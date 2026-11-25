package com.example.rynzodriver.data.repository.websocket

// Senior explanation:
// Repository implementations that bridge APIs/services into domain contracts.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements domain repository contracts using data sources.

// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.websocket.WebSocketManager
import com.example.rynzodriver.data.websocket.WebSocketState
import com.example.rynzodriver.domain.repository.websocket.WebSocketRepository
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
@Singleton
// Runtime type: `WebSocketRepositoryImpl` owns the properties and methods declared in this class body.
class WebSocketRepositoryImpl @Inject constructor(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val webSocketManager: WebSocketManager
) : WebSocketRepository {

// Read-only state exposure: callers can collect but should not mutate this flow; mutation remains inside the owning class.
    override val connectionState: StateFlow<WebSocketState> = webSocketManager.connectionState
// Event stream: `SharedFlow` broadcasts values to active collectors and is commonly used for one-time UI events.
    override val incomingMessages: SharedFlow<String> = webSocketManager.incomingMessages

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `connect`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
    override fun connect(url: String) {
        webSocketManager.connect(url)
    }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `sendMessage`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
    override fun sendMessage(message: String): Boolean {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return webSocketManager.sendMessage(message)
    }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `disconnect`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
    override fun disconnect() {
        webSocketManager.disconnect()
    }
}
