package com.example.rynzodriver.data.repository.websocket

import com.example.rynzodriver.data.websocket.WebSocketManager
import com.example.rynzodriver.data.websocket.WebSocketState
import com.example.rynzodriver.domain.repository.websocket.WebSocketRepository
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketRepositoryImpl @Inject constructor(
    private val webSocketManager: WebSocketManager
) : WebSocketRepository {

    override val connectionState: StateFlow<WebSocketState> = webSocketManager.connectionState
    override val incomingMessages: SharedFlow<String> = webSocketManager.incomingMessages

    override fun connect(url: String) {
        webSocketManager.connect(url)
    }

    override fun sendMessage(message: String): Boolean {
        return webSocketManager.sendMessage(message)
    }

    override fun disconnect() {
        webSocketManager.disconnect()
    }
}
