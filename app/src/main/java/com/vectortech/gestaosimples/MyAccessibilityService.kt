package com.vectortech.gestaosimples

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.content.Intent
import android.util.Log

class MyAccessibilityService : AccessibilityService() {

    private lateinit var prefsManager: PrefsManager

    override fun onServiceConnected() {
        super.onServiceConnected()
        prefsManager = PrefsManager(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Garante que o PrefsManager esteja inicializado
        if (!::prefsManager.isInitialized) {
            prefsManager = PrefsManager(this)
        }

        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()
            Log.d("GestaoSimples", "App aberto: $packageName")

            if (packageName != null) {
                // Safety: if blocking is disabled or not configured yet, do nothing
                if (!prefsManager.isBlockingEnabled()) return

                val blockedApps = prefsManager.getBlockedApps()
                // Always allow Settings when not configured
                val safeAllowWhenUnconfigured = setOf(
                    "com.android.settings",
                    "com.google.android.packageinstaller"
                )
                if (blockedApps.contains(packageName)) {
                    // If package is Settings and user needs to disable admin, still block only when configured
                    showLockScreen()
                } else if (safeAllowWhenUnconfigured.contains(packageName) && blockedApps.isEmpty()) {
                    return
                }
            }
        }
    }

    private fun showLockScreen() {
        val intent = Intent(this, LockActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onInterrupt() {
        // Serviço interrompido
    }
}
