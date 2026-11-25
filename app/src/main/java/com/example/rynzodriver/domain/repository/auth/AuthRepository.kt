package com.example.rynzodriver.domain.repository.auth

// Senior explanation:
// Framework-independent repository contracts.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file declares domain repository contracts.

// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.domain.model.User
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import kotlinx.coroutines.flow.Flow

// Contract: `AuthRepository` declares capabilities. Concrete classes implement these methods without changing callers.
interface AuthRepository {
// Function `login`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun login(mobileNumber: String, password: String): Result<User>
// Function `refreshAccessToken`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun refreshAccessToken(): Result<String>
// Function `isLoggedIn`: normal synchronous execution is used unless the body starts a coroutine itself.
// Asynchronous stream: `Flow` represents a sequence of values delivered over time rather than one immediate result.
    fun isLoggedIn(): Flow<Boolean>
}
