package com.example.rynzodriver.data.repository.auth

// Senior explanation:
// Repository implementations that bridge APIs/services into domain contracts.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements domain repository contracts using data sources.

// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.api.auth.AuthApiService
import com.example.rynzodriver.data.dto.auth.LoginRequest
import com.example.rynzodriver.data.dto.auth.RefreshTokenRequest
import com.example.rynzodriver.data.local.DataStoreManager
import com.example.rynzodriver.domain.model.User
import com.example.rynzodriver.domain.repository.auth.AuthRepository
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Runtime type: `AuthRepositoryImpl` owns the properties and methods declared in this class body.
class AuthRepositoryImpl @Inject constructor(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val authApiService: AuthApiService,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val dataStoreManager: DataStoreManager
) : AuthRepository {

    override suspend fun login(mobileNumber: String, password: String): Result<User> {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Exception boundary: operations here may throw; success continues normally and matching `catch` blocks handle failures.
        return try {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val response = authApiService.login(LoginRequest(mobileNumber, password))
// `if` guard: the following block executes only while `response.isSuccessful && response.body() != null` evaluates to `true`; a false result skips it.
            if (response.isSuccessful && response.body() != null) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Explicit non-null assertion: `!!` tells Kotlin the value cannot be null; null at runtime throws an exception instead.
                val loginResponse = response.body()!!
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val userData = loginResponse.data.user
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val tokens = loginResponse.data.tokens

                dataStoreManager.saveAuthData(
                    id = userData.id,
                    name = userData.name,
                    mobileNumber = userData.mobileNumber,
                    role = userData.role,
                    accessToken = tokens.accessToken,
                    refreshToken = tokens.refreshToken
                )

// Kotlin Result: this stores success or failure as a typed value instead of requiring an immediate thrown exception.
                Result.success(
                    User(
                        id = userData.id,
                        name = userData.name,
                        mobileNumber = userData.mobileNumber,
                        role = userData.role,
                        token = tokens.accessToken
                    )
                )
            } else {
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                Result.failure(Exception(response.body()?.message ?: "Login failed"))
            }
// Failure recovery: this catch block receives the thrown exception and can update state, notify the UI, or rethrow.
        } catch (e: Exception) {
// Kotlin Result: this stores success or failure as a typed value instead of requiring an immediate thrown exception.
            Result.failure(e)
        }
    }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `isLoggedIn`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
// Asynchronous stream: `Flow` represents a sequence of values delivered over time rather than one immediate result.
    override fun isLoggedIn(): Flow<Boolean> {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Collection transformation: `map` returns a new list with one transformed result per item; `mapIndexed` also supplies each index.
        return dataStoreManager.accessToken.map { !it.isNullOrEmpty() }
    }

    override suspend fun refreshAccessToken(): Result<String> {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Exception boundary: operations here may throw; success continues normally and matching `catch` blocks handle failures.
        return try {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val refreshToken = dataStoreManager.refreshToken.first()
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
// `if` guard: the following block executes only while `refreshToken.isNullOrBlank()` evaluates to `true`; a false result skips it.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
            if (refreshToken.isNullOrBlank()) {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Kotlin Result: this stores success or failure as a typed value instead of requiring an immediate thrown exception.
                return Result.failure(Exception("No refresh token available"))
            }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val response = authApiService.refreshToken(RefreshTokenRequest(refreshToken))
// `if` guard: the following block executes only while `response.isSuccessful && response.body() != null` evaluates to `true`; a false result skips it.
            if (response.isSuccessful && response.body() != null) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Explicit non-null assertion: `!!` tells Kotlin the value cannot be null; null at runtime throws an exception instead.
                val newAccessToken = response.body()!!.accessToken
                dataStoreManager.updateAccessToken(newAccessToken)
// Kotlin Result: this stores success or failure as a typed value instead of requiring an immediate thrown exception.
                Result.success(newAccessToken)
            } else {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                val errorMsg = response.errorBody()?.string() ?: "Failed to refresh token"
// Kotlin Result: this stores success or failure as a typed value instead of requiring an immediate thrown exception.
                Result.failure(Exception(errorMsg))
            }
// Failure recovery: this catch block receives the thrown exception and can update state, notify the UI, or rethrow.
        } catch (e: Exception) {
// Kotlin Result: this stores success or failure as a typed value instead of requiring an immediate thrown exception.
            Result.failure(e)
        }
    }
}
