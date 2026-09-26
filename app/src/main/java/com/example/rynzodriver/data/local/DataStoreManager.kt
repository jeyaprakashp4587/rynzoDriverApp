package com.example.rynzodriver.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_MOBILE = stringPreferencesKey("user_mobile")
        val USER_ROLE = stringPreferencesKey("user_role")
    }

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

    val accessToken: Flow<String?> = context.dataStore.data.map { it[ACCESS_TOKEN] }
    
    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID] }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { 
        !it[ACCESS_TOKEN].isNullOrEmpty() 
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
