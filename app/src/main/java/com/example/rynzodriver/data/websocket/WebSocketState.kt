package com.example.rynzodriver.data.websocket

sealed interface WebSocketState {
    data object Disconnected : WebSocketState
    data object Connecting : WebSocketState
    data object Connected : WebSocketState
    data class Error(val throwable: Throwable) : WebSocketState
}
