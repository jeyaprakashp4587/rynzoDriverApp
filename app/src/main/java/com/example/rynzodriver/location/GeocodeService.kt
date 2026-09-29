package com.example.rynzodriver.location

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Reverse geocode using OpenStreetMap Nominatim (no API key required).
 * Sets a User-Agent header per Nominatim usage policy.
 */
suspend fun reverseGeocode(lat: Double, lng: Double): String? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL("https://nominatim.openstreetmap.org/reverse?lat=$lat&lon=$lng&format=jsonv2")
            val conn = url.openConnection() as HttpURLConnection
            conn.setRequestProperty("User-Agent", "RynzoDriver/1.0 (contact@example.com)")
            conn.connectTimeout = 5000
            conn.readTimeout = 5000
            conn.requestMethod = "GET"
            conn.connect()
            val text = conn.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(text)
            json.optString("display_name", null)
        } catch (e: Exception) {
            null
        }
    }
}
