package com.example.rynzodriver.data.websocket

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketManager @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    private var webSocket: WebSocket? = null

    private val _connectionState = MutableStateFlow<WebSocketState>(WebSocketState.Disconnected)
    val connectionState: StateFlow<WebSocketState> = _connectionState.asStateFlow()

    private val _incomingMessages = MutableSharedFlow<String>(extraBufferCapacity = 64)
    val incomingMessages: SharedFlow<String> = _incomingMessages.asSharedFlow()

    private val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("WebSocketManager", "WebSocket Connected")
            _connectionState.value = WebSocketState.Connected
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocketManager", "Message received: $text")
            _incomingMessages.tryEmit(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            val text = bytes.utf8()
            Log.d("WebSocketManager", "Byte message received: $text")
            _incomingMessages.tryEmit(text)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocketManager", "WebSocket Closing: $reason ($code)")
            webSocket.close(1000, null)
            _connectionState.value = WebSocketState.Disconnected
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocketManager", "WebSocket Closed: $reason ($code)")
            _connectionState.value = WebSocketState.Disconnected
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("WebSocketManager", "WebSocket Error", t)
            _connectionState.value = WebSocketState.Error(t)
        }
    }

    fun connect(url: String) {
        if (_connectionState.value is WebSocketState.Connected || _connectionState.value is WebSocketState.Connecting) {
            return
        }
        _connectionState.value = WebSocketState.Connecting
        val request = Request.Builder().url(url).build()
        webSocket = okHttpClient.newWebSocket(request, listener)
    }

    fun sendMessage(text: String): Boolean {
        return webSocket?.send(text) ?: false
    }

    fun disconnect() {
        webSocket?.close(1000, "Client disconnected")
        webSocket = null
        _connectionState.value = WebSocketState.Disconnected
    }
}
