package com.example.rynzodriver.data.local

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DataStoreManagerTest {
    @Test
    fun hasValidSession_requiresBothTokenAndUserId() {
        assertTrue(DataStoreManager.hasValidSession("access-token", "user-123"))
        assertFalse(DataStoreManager.hasValidSession(null, "user-123"))
        assertFalse(DataStoreManager.hasValidSession("access-token", null))
        assertFalse(DataStoreManager.hasValidSession("", "user-123"))
        assertFalse(DataStoreManager.hasValidSession("access-token", ""))
    }
}
