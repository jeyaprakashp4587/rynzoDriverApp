package com.example.rynzodriver.ui.home

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.rynzodriver.location.getCurrentOrLastLocation
import com.example.rynzodriver.location.reverseGeocode
import kotlinx.coroutines.launch

@Composable
fun LocationProvider(fetchWhen: Boolean, content: @Composable (String?) -> Unit) {
    val context = LocalContext.current
    var address by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(fetchWhen) {
        if (fetchWhen) {
            scope.launch {
                val loc = getCurrentOrLastLocation(context)
                if (loc != null) {
                    val name = reverseGeocode(loc.latitude, loc.longitude)
                    address = name
                }
            }
        }
    }

    content(address)
}
