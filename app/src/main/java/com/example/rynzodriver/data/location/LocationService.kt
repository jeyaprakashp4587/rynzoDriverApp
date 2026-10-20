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
import com.example.rynzodriver.BuildConfig
import com.example.rynzodriver.R
import com.example.rynzodriver.data.dto.LocationUpdateDto
import com.example.rynzodriver.data.local.DataStoreManager
import com.example.rynzodriver.data.websocket.WebSocketState
import com.example.rynzodriver.domain.repository.websocket.WebSocketRepository
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var activeVehicleId: String? = null

    @Inject
    lateinit var locationClient: LocationClient

    @Inject
    lateinit var webSocketRepository: WebSocketRepository

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start(intent)
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(startIntent: Intent?) {
        val vehicleId = startIntent?.getStringExtra(EXTRA_VEHICLE_ID)
        if (vehicleId.isNullOrBlank()) {
            Log.w("LocationService", "Vehicle ID missing; location tracking will not start.")
            stopSelf()
            return
        }

        activeVehicleId = vehicleId
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
        serviceScope.launch {
            val driverId = dataStoreManager.userId.first() ?: run {
                Log.e("LocationService", "Missing userId for websocket connection")
                return@launch
            }

            val liveBaseUrl = BuildConfig.LIVE_TRACKING_WS_URL.trimEnd('/')
            val wsUrl = liveBaseUrl + "/connect?userId=$driverId"
            Log.d("LocationService", "Connecting to live tracking WebSocket: $wsUrl")
            webSocketRepository.connect(wsUrl)

            val connected = webSocketRepository.connectionState.first { it is WebSocketState.Connected }
            Log.d("LocationService", "WebSocket connected state reached: $connected")

            val vehicleId = activeVehicleId
            if (!vehicleId.isNullOrBlank()) {
                val joinMessage = """{"type":"joinVehicle","vehicleId":"$vehicleId"}"""
                val joined = webSocketRepository.sendMessage(joinMessage)
                Log.d("LocationService", "Join vehicle room result: $joined -> $joinMessage")
            }
        }
    }

    private fun sendLocationViaWebSocket(lat: Double, long: Double) {
        serviceScope.launch {
            try {
                val vehicleId = activeVehicleId
                if (vehicleId.isNullOrBlank()) {
                    Log.w("LocationService", "Skip socket send because vehicleId is missing")
                    return@launch
                }

                if (webSocketRepository.connectionState.value !is WebSocketState.Connected) {
                    Log.w("LocationService", "WebSocket not connected yet. Skipping location send for vehicleId=$vehicleId")
                    return@launch
                }

                val locationData = LocationUpdateDto(
                    vehicleId = vehicleId,
                    lat = lat,
                    lon = long,
                    time = System.currentTimeMillis().toString()
                )
                val jsonMessage = gson.toJson(locationData)
                val sent = webSocketRepository.sendMessage(jsonMessage)
                Log.d("LocationService", "Sending location result: $sent -> $jsonMessage")
                if (!sent) {
                    Log.w("LocationService", "Location socket send failed; connection may not be open yet")
                }
            } catch (e: Exception) {
                Log.e("LocationService", "Error sending location via WebSocket", e)
                e.printStackTrace()
            }
        }
    }

    private fun stop() {
        Log.d("LocationService", "Stopping location tracking")
        activeVehicleId = null
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
        const val EXTRA_VEHICLE_ID = "extra_vehicle_id"
        private const val CHANNEL_ID = "location"
        private const val NOTIFICATION_ID = 1
    }
}
