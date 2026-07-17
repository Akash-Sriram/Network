package de.mangelow.network

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class RadioNetwork : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Remove standard entry/exit transition animations for instant launch
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, 0, 0)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, 0)
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(0, 0)
        }
        
        launchRadioInfoSettings(this)
        finish()
    }

    private fun launchRadioInfoSettings(context: Context) {
        val intents = listOf(
            // Intent 1: settings package with ACTION_MAIN and RadioInfo class
            Intent("android.intent.action.MAIN").apply {
                setClassName("com.android.settings", "com.android.settings.RadioInfo")
            },
            // Intent 2: phone package with ACTION_MAIN and RadioInfo class
            Intent("android.intent.action.MAIN").apply {
                setClassName("com.android.phone", "com.android.phone.settings.RadioInfo")
            },
            // Intent 3: settings package without action
            Intent().apply {
                setClassName("com.android.settings", "com.android.settings.RadioInfo")
            },
            // Intent 4: phone package without action
            Intent().apply {
                setClassName("com.android.phone", "com.android.phone.settings.RadioInfo")
            }
        )

        var success = false
        for (intent in intents) {
            try {
                context.startActivity(intent)
                success = true
                break
            } catch (e: Exception) {
                Log.e("RadioNetwork", "Intent launch failed: ${intent.component}", e)
            }
        }

        if (!success) {
            Toast.makeText(context, "Could not open Radio Info settings on this device.", Toast.LENGTH_LONG).show()
        }
    }
}
