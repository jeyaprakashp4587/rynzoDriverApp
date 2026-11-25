package com.example.rynzodriver.domain.usecase

// Senior explanation:
// Small business operations delegated to repository implementations.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file defines a focused business operation.

// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.domain.model.User
import com.example.rynzodriver.domain.repository.auth.AuthRepository
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import javax.inject.Inject

// Runtime type: `LoginUseCase` owns the properties and methods declared in this class body.
class LoginUseCase @Inject constructor(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val repository: AuthRepository
) {
// Function `invoke`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function; `operator` enables special call syntax such as invocation or arithmetic.
    suspend operator fun invoke(mobile: String, password: String): Result<User> {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return repository.login(mobile, password)
    }
}
