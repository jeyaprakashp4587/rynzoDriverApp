package com.example.rynzodriver.data.local

// Senior explanation:
// Local authentication preferences and Room database persistence.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements local persistence for authentication or database records.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.content.Context
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
// Hilt imports: dependency injection annotations and ViewModel integration.
import dagger.hilt.android.qualifiers.ApplicationContext
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Delegated property: `by` delegates reading/writing to the expression to the right. The delegate owns storage and observation semantics.
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
@Singleton
// Runtime type: `DataStoreManager` owns the properties and methods declared in this class body.
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

// Companion: members here belong to the surrounding class and are callable without constructing an instance.
    companion object {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val USER_ID = stringPreferencesKey("user_id")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val USER_NAME = stringPreferencesKey("user_name")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val USER_MOBILE = stringPreferencesKey("user_mobile")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val USER_ROLE = stringPreferencesKey("user_role")

// Function `hasValidSession`: normal synchronous execution is used unless the body starts a coroutine itself.
        fun hasValidSession(accessToken: String?, userId: String?): Boolean {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
            return !accessToken.isNullOrBlank() && !userId.isNullOrBlank()
        }
    }

// Function `saveAuthData`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun saveAuthData(
        id: String,
        name: String,
        mobileNumber: String,
        role: String,
        accessToken: String,
        refreshToken: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = id
            preferences[USER_NAME] = name
            preferences[USER_MOBILE] = mobileNumber
            preferences[USER_ROLE] = role
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Asynchronous stream: `Flow` represents a sequence of values delivered over time rather than one immediate result.
    val accessToken: Flow<String?> = context.dataStore.data.map { it[ACCESS_TOKEN] }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Asynchronous stream: `Flow` represents a sequence of values delivered over time rather than one immediate result.
    val refreshToken: Flow<String?> = context.dataStore.data.map { it[REFRESH_TOKEN] }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Asynchronous stream: `Flow` represents a sequence of values delivered over time rather than one immediate result.
    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID] }

// Function `updateAccessToken`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun updateAccessToken(newAccessToken: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = newAccessToken
        }
    }

// Function `hasValidSession`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun hasValidSession(): Boolean {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val preferences = context.dataStore.data.first()
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return hasValidSession(preferences[ACCESS_TOKEN], preferences[USER_ID])
    }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Asynchronous stream: `Flow` represents a sequence of values delivered over time rather than one immediate result.
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        hasValidSession(preferences[ACCESS_TOKEN], preferences[USER_ID])
    }

// Function `clearAll`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
