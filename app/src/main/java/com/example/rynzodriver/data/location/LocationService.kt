package com.example.rynzodriver.data.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.rynzodriver.R
import com.example.rynzodriver.data.dto.LocationUpdateDto
import com.example.rynzodriver.domain.repository.websocket.WebSocketRepository
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Inject
    lateinit var locationClient: LocationClient

    @Inject
    lateinit var webSocketRepository: WebSocketRepository

    @Inject
    lateinit var gson: Gson

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        // Connect to WebSocket
        connectToWebSocket()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Tracking location...")
            .setContentText("Location: Unknown")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(10000L) // 10 sec heartbeat
            .catch { e ->
                Log.e("LocationService", "Location update error", e)
                e.printStackTrace()
            }
            .onEach { location ->
                val lat = location.latitude
                val long = location.longitude
                LocationTrackingState.updateLocation(lat, long)

                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                notificationManager.notify(NOTIFICATION_ID, updatedNotification.build())

                sendLocationViaWebSocket(lat, long)
            }
            .launchIn(serviceScope)

        startForeground(NOTIFICATION_ID, notification.build())
    }

    private fun connectToWebSocket() {
        val wsUrl = "ws://192.168.1.23:8000/driver/location"
        Log.d("LocationService", "Connecting to WebSocket: $wsUrl")
        webSocketRepository.connect(wsUrl)
    }

    private fun sendLocationViaWebSocket(lat: Double, long: Double) {
        serviceScope.launch {
            try {
                val locationData = LocationUpdateDto(
                    latitude = lat,
                    longitude = long,
                    timestamp = System.currentTimeMillis()
                )
                val jsonMessage = gson.toJson(locationData)
                Log.d("LocationService", "Sending location: $jsonMessage")
                webSocketRepository.sendMessage(jsonMessage)
            } catch (e: Exception) {
                Log.e("LocationService", "Error sending location via WebSocket", e)
                e.printStackTrace()
            }
        }
    }

    private fun stop() {
        Log.d("LocationService", "Stopping location tracking")
        LocationTrackingState.reset()
        webSocketRepository.disconnect()
        stopForeground(true)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        private const val CHANNEL_ID = "location"
        private const val NOTIFICATION_ID = 1
    }
}
