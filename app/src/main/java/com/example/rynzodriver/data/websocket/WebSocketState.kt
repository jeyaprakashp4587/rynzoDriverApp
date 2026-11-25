package com.example.rynzodriver.data.websocket

// Senior explanation:
// OkHttp WebSocket lifecycle, connection state, and message delivery.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file manages WebSocket connection state and messages.

// Restricted type hierarchy: `{` only permits implementations/types listed in this module, enabling exhaustive `when` handling.
sealed interface WebSocketState {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    data object Disconnected : WebSocketState
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    data object Connecting : WebSocketState
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    data object Connected : WebSocketState
// Data model: `Error` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
    data class Error(val throwable: Throwable) : WebSocketState
}
