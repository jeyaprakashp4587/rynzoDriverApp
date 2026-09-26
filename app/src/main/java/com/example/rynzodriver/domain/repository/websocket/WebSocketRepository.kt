package com.example.rynzodriver.domain.repository.websocket

import com.example.rynzodriver.data.websocket.WebSocketState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface WebSocketRepository {
    val connectionState: StateFlow<WebSocketState>
    val incomingMessages: SharedFlow<String>

    fun connect(url: String)
    fun sendMessage(message: String): Boolean
    fun disconnect()
}
