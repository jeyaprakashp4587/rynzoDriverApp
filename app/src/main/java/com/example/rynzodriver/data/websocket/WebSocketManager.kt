package com.example.rynzodriver.data.websocket

// Senior explanation:
// OkHttp WebSocket lifecycle, connection state, and message delivery.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file manages WebSocket connection state and messages.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.util.Log
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import kotlinx.coroutines.flow.MutableSharedFlow
// Mutable observable state: `MutableStateFlow` owns the latest value and emits updates to collectors.
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
// OkHttp/Okio imports: network transport, request interception, and binary/text data.
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import javax.inject.Inject
import javax.inject.Singleton

// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
@Singleton
// Runtime type: `WebSocketManager` owns the properties and methods declared in this class body.
class WebSocketManager @Inject constructor(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val okHttpClient: OkHttpClient
) {
// Mutable property: `var` permits reassignment. Any observer is responsible for reacting if this value changes.
    private var webSocket: WebSocket? = null

// Reactive state: this value owns a latest StateFlow value and emits updates to active collectors.
// Mutable observable state: `MutableStateFlow` owns the latest value and emits updates to collectors.
    private val _connectionState = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected)
// Reactive state: this value owns a latest StateFlow value and emits updates to active collectors.
// Read-only state exposure: callers can collect but should not mutate this flow; mutation remains inside the owning class.
    val connectionState: StateFlow<WebSocketState> = _connectionState.asStateFlow()

// Reactive stream: this property represents values produced asynchronously over time.
// Event stream: `SharedFlow` broadcasts values to active collectors and is commonly used for one-time UI events.
    private val _incomingMessages = MutableSharedFlow<String>(extraBufferCapacity = 64)
// Reactive stream: this property represents values produced asynchronously over time.
// Event stream: `SharedFlow` broadcasts values to active collectors and is commonly used for one-time UI events.
    val incomingMessages: SharedFlow<String> = _incomingMessages.asSharedFlow()

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val listener = object : WebSocketListener() {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `onOpen`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("WebSocketManager", "WebSocket Connected")
            _connectionState.value = WebSocketState.Connected
        }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `onMessage`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocketManager", "Message received: $text")
            _incomingMessages.tryEmit(text)
        }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `onMessage`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val text = bytes.utf8()
            Log.d("WebSocketManager", "Byte message received: $text")
            _incomingMessages.tryEmit(text)
        }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `onClosing`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocketManager", "WebSocket Closing: $reason ($code)")
            webSocket.close(1000, null)
            _connectionState.value = WebSocketState.Disconnected
        }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `onClosed`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocketManager", "WebSocket Closed: $reason ($code)")
            _connectionState.value = WebSocketState.Disconnected
        }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `onFailure`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("WebSocketManager", "WebSocket Error", t)
            _connectionState.value = WebSocketState.Error(t)
        }
    }

// Function `connect`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun connect(url: String) {
// `if` guard: the following block executes only while `_connectionState.value is WebSocketState.Connected || _connectionState.value is WebSocketState.Connecting` evaluates to `true`; a false result skips it.
        if (_connectionState.value is WebSocketState.Connected || _connectionState.value is WebSocketState.Connecting) {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
            return
        }
        _connectionState.value = WebSocketState.Connecting
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val request = Request.Builder().url(url).build()
        webSocket = okHttpClient.newWebSocket(request, listener)
    }

// Function `sendMessage`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun sendMessage(text: String): Boolean {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
        return webSocket?.send(text) ?: false
    }

// Function `disconnect`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun disconnect() {
        webSocket?.close(1000, "Client disconnected")
        webSocket = null
        _connectionState.value = WebSocketState.Disconnected
    }
}
