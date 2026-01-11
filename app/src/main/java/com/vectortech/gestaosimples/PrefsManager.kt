package com.vectortech.gestaosimples

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("GestaoSimplesPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CHILD_PASSWORD = "child_password"
        private const val KEY_BLOCKED_APPS = "blocked_apps"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_PARENT_EMAIL = "parent_email"
        private const val KEY_DEVICE_ID = "device_id"
    }

    fun setChildPassword(password: String) {
        prefs.edit().putString(KEY_CHILD_PASSWORD, password).apply()
    }

    fun getChildPassword(): String {
        return prefs.getString(KEY_CHILD_PASSWORD, "1234") ?: "1234"
    }

    fun setBlockedApps(packageNames: List<String>) {
        val set = packageNames.toSet()
        prefs.edit().putStringSet(KEY_BLOCKED_APPS, set).apply()
    }

    fun getBlockedApps(): List<String> {
        val set = prefs.getStringSet(KEY_BLOCKED_APPS, null)
        return set?.toList() ?: emptyList()
    }

    fun setLoggedIn(email: String) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_PARENT_EMAIL, email)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    fun getParentEmail(): String {
        return prefs.getString(KEY_PARENT_EMAIL, "") ?: ""
    }
    
    fun setDeviceId(deviceId: String) {
        prefs.edit().putString(KEY_DEVICE_ID, deviceId).apply()
    }

    fun getDeviceId(): String {
        return prefs.getString(KEY_DEVICE_ID, "") ?: ""
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
