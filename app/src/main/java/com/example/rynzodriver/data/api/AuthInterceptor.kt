package com.example.rynzodriver.data.api

// Senior explanation:
// Retrofit endpoint contracts and HTTP request interception.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file defines HTTP API contracts or request authentication behavior.

// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.local.DataStoreManager
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
// OkHttp/Okio imports: network transport, request interception, and binary/text data.
import okhttp3.Interceptor
import okhttp3.Response
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import javax.inject.Inject

// Runtime type: `AuthInterceptor` owns the properties and methods declared in this class body.
class AuthInterceptor @Inject constructor(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val dataStoreManager: DataStoreManager
) : Interceptor {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `intercept`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
    override fun intercept(chain: Interceptor.Chain): Response {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val token = runBlocking { dataStoreManager.accessToken.first() }
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val request = chain.request().newBuilder().apply {
// `if` guard: the following block executes only while `!token.isNullOrEmpty()` evaluates to `true`; a false result skips it.
            if (!token.isNullOrEmpty()) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return chain.proceed(request)
    }
}
