package com.example.rynzodriver.domain.repository.websocket

// Senior explanation:
// Framework-independent repository contracts.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file declares domain repository contracts.

// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.websocket.WebSocketState
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

// Contract: `WebSocketRepository` declares capabilities. Concrete classes implement these methods without changing callers.
interface WebSocketRepository {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Read-only state exposure: callers can collect but should not mutate this flow; mutation remains inside the owning class.
    val connectionState: StateFlow<WebSocketState>
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Event stream: `SharedFlow` broadcasts values to active collectors and is commonly used for one-time UI events.
    val incomingMessages: SharedFlow<String>

// Function `connect`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun connect(url: String)
// Function `sendMessage`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun sendMessage(message: String): Boolean
// Function `disconnect`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun disconnect()
}
