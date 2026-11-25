package com.example.rynzodriver.data.location

// Senior explanation:
// Android location updates, foreground tracking, and location state.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file manages Android location updates and location tracking state.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import androidx.core.app.NotificationCompat
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.BuildConfig
import com.example.rynzodriver.R
import com.example.rynzodriver.data.dto.LocationUpdateDto
import com.example.rynzodriver.data.local.DataStoreManager
import com.example.rynzodriver.data.websocket.WebSocketState
import com.example.rynzodriver.domain.repository.auth.AuthRepository
import com.example.rynzodriver.domain.repository.websocket.WebSocketRepository
// Serialization imports: converting JSON or structured data to/from Kotlin objects.
import com.google.gson.Gson
// Hilt imports: dependency injection annotations and ViewModel integration.
import dagger.hilt.android.AndroidEntryPoint
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
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

// Hilt integration: Android creates this component with a generated dependency graph.
@AndroidEntryPoint
// Runtime type: `LocationService` owns the properties and methods declared in this class body.
class LocationService : Service() {

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
// Mutable property: `var` permits reassignment. Any observer is responsible for reacting if this value changes.
    private var activeVehicleId: String? = null

// Constructor injection: Hilt supplies dependencies when it constructs this declaration.
    @Inject
    lateinit var locationClient: LocationClient

// Constructor injection: Hilt supplies dependencies when it constructs this declaration.
    @Inject
    lateinit var webSocketRepository: WebSocketRepository

// Constructor injection: Hilt supplies dependencies when it constructs this declaration.
    @Inject
    lateinit var gson: Gson

// Constructor injection: Hilt supplies dependencies when it constructs this declaration.
    @Inject
    lateinit var dataStoreManager: DataStoreManager

// Constructor injection: Hilt supplies dependencies when it constructs this declaration.
    @Inject
    lateinit var authRepository: AuthRepository

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `onBind`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
    override fun onBind(p0: Intent?): IBinder? {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return null
    }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `onCreate`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        observeWebSocketMessages()
    }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `onStartCommand`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
// `when` branch selection: Kotlin evaluates conditions in source order and executes only the first matching branch.
// Branch selection: `when` evaluates branches in source order and executes only the first matching branch.
        when(intent?.action) {
            ACTION_START -> start(intent)
            ACTION_STOP -> stop()
        }
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return super.onStartCommand(intent, flags, startId)
    }

// Function `start`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
    private fun start(startIntent: Intent?) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val vehicleId = startIntent?.getStringExtra(EXTRA_VEHICLE_ID)
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
// `if` guard: the following block executes only while `vehicleId.isNullOrBlank()` evaluates to `true`; a false result skips it.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
        if (vehicleId.isNullOrBlank()) {
            Log.w("LocationService", "Vehicle ID missing; location tracking will not start.")
            stopSelf()
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
            return
        }

        activeVehicleId = vehicleId
        connectToWebSocket()

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Tracking location...")
            .setContentText("Location: Unknown")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(10000L) // 10 sec heartbeat
            .catch { e ->
                Log.e("LocationService", "Location update error", e)
                e.printStackTrace()
            }
            .onEach { location ->
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val lat = location.latitude
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val long = location.longitude
                LocationTrackingState.updateLocation(lat, long)

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                notificationManager.notify(NOTIFICATION_ID, updatedNotification.build())

                sendLocationViaWebSocket(lat, long)
            }
            .launchIn(serviceScope)

        startForeground(NOTIFICATION_ID, notification.build())
    }

// Function `connectToWebSocket`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
    private fun connectToWebSocket() {
        serviceScope.launch {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val token = dataStoreManager.accessToken.first()
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
// `if` guard: the following block executes only while `token.isNullOrBlank()` evaluates to `true`; a false result skips it.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
            if (token.isNullOrBlank()) {
                Log.e("LocationService", "Missing accessToken for websocket connection")
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
                return@launch
            }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val liveBaseUrl = BuildConfig.LIVE_TRACKING_WS_URL.trimEnd('/')
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val wsUrl = "$liveBaseUrl/connect?token=$token"
            Log.d("LocationService", "Connecting to live tracking WebSocket: $wsUrl")
            webSocketRepository.connect(wsUrl)

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val connected = webSocketRepository.connectionState.first { it is WebSocketState.Connected }
            Log.d("LocationService", "WebSocket connected state reached: $connected")

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val authMessage = """{"auth":{"token":"$token"}}"""
            webSocketRepository.sendMessage(authMessage)

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val vehicleId = activeVehicleId
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
// `if` guard: the following block executes only while `!vehicleId.isNullOrBlank()` evaluates to `true`; a false result skips it.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
            if (!vehicleId.isNullOrBlank()) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val joinMessage = """{"type":"joinVehicle","vehicleId":"$vehicleId"}"""
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val joined = webSocketRepository.sendMessage(joinMessage)
                Log.d("LocationService", "Join vehicle room result: $joined -> $joinMessage")
            }
        }
    }

// Function `observeWebSocketMessages`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
    private fun observeWebSocketMessages() {
        webSocketRepository.incomingMessages
            .onEach { message ->
                Log.d("LocationService", "WebSocket incoming message: $message")
// `if` guard: the following block executes only while `message.contains("AUTH_TOKEN_EXPIRED") || message.contains("AUTHENTICATION_REQUIRED")` evaluates to `true`; a false result skips it.
                if (message.contains("AUTH_TOKEN_EXPIRED") || message.contains("AUTHENTICATION_REQUIRED")) {
                    handleTokenExpirationAndReconnect()
                }
            }
            .launchIn(serviceScope)
    }

// Function `handleTokenExpirationAndReconnect`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
    private fun handleTokenExpirationAndReconnect() {
        serviceScope.launch {
            Log.w("LocationService", "Access token expired according to Rust WebSocket server. Refreshing token via Node.js...")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val refreshResult = authRepository.refreshAccessToken()
// Kotlin Result: this stores success or failure as a typed value instead of requiring an immediate thrown exception.
            refreshResult.onSuccess { newAccessToken ->
                Log.i("LocationService", "Successfully refreshed access token via Node.js. Reconnecting WebSocket...")
                webSocketRepository.disconnect()
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val liveBaseUrl = BuildConfig.LIVE_TRACKING_WS_URL.trimEnd('/')
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val wsUrl = "$liveBaseUrl/connect?token=$newAccessToken"
                webSocketRepository.connect(wsUrl)
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val connected = webSocketRepository.connectionState.first { it is WebSocketState.Connected }
// `if` guard: the following block executes only while `connected is WebSocketState.Connected` evaluates to `true`; a false result skips it.
                if (connected is WebSocketState.Connected) {
                    webSocketRepository.sendMessage("""{"auth":{"token":"$newAccessToken"}}""")
// Safe-call chain: `?.let` runs the block only when the receiver is non-null; inside the block `it` is the non-null value.
                    activeVehicleId?.let { vId ->
                        webSocketRepository.sendMessage("""{"type":"joinVehicle","vehicleId":"$vId"}""")
                    }
                }
            }.onFailure { e ->
                Log.e("LocationService", "Failed to refresh token via Node.js: ${e.message}. Stopping tracking session.", e)
                dataStoreManager.clearAll()
                stop()
            }
        }
    }

// Function `sendLocationViaWebSocket`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
    private fun sendLocationViaWebSocket(lat: Double, long: Double) {
        serviceScope.launch {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// `try` boundary: execution continues normally for success; any thrown exception may be captured by a matching `catch`.
// Exception boundary: operations here may throw; success continues normally and matching `catch` blocks handle failures.
            try {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val vehicleId = activeVehicleId
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
// `if` guard: the following block executes only while `vehicleId.isNullOrBlank()` evaluates to `true`; a false result skips it.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
                if (vehicleId.isNullOrBlank()) {
                    Log.w("LocationService", "Skip socket send because vehicleId is missing")
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
                    return@launch
                }

// `if` guard: the following block executes only while `webSocketRepository.connectionState.value !is WebSocketState.Connected` evaluates to `true`; a false result skips it.
                if (webSocketRepository.connectionState.value !is WebSocketState.Connected) {
                    Log.w("LocationService", "WebSocket not connected yet. Skipping location send for vehicleId=$vehicleId")
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
                    return@launch
                }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val locationData = LocationUpdateDto(
                    vehicleId = vehicleId,
                    lat = lat,
                    lon = long,
                    time = System.currentTimeMillis().toString()
                )
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val jsonMessage = gson.toJson(locationData)
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val sent = webSocketRepository.sendMessage(jsonMessage)
                Log.d("LocationService", "Sending location result: $sent -> $jsonMessage")
// `if` guard: the following block executes only while `!sent` evaluates to `true`; a false result skips it.
                if (!sent) {
                    Log.w("LocationService", "Location socket send failed; connection may not be open yet")
                }
// Failure recovery: this catch block receives the thrown exception and can update state, notify the UI, or rethrow.
            } catch (e: Exception) {
                Log.e("LocationService", "Error sending location via WebSocket", e)
                e.printStackTrace()
            }
        }
    }

// Function `stop`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
    private fun stop() {
        Log.d("LocationService", "Stopping location tracking")
        activeVehicleId = null
        LocationTrackingState.reset()
        webSocketRepository.disconnect()
        stopForeground(true)
        stopSelf()
    }

// Function `createNotificationChannel`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
    private fun createNotificationChannel() {
// `if` guard: the following block executes only while `Build.VERSION.SDK_INT >= Build.VERSION_CODES.O` evaluates to `true`; a false result skips it.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `onDestroy`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

// Companion: members here belong to the surrounding class and are callable without constructing an instance.
    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val EXTRA_VEHICLE_ID = "extra_vehicle_id"
        private const val CHANNEL_ID = "location"
        private const val NOTIFICATION_ID = 1
    }
}
