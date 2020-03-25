package com.birdeveloper.androidutils

import android.app.ActivityManager
import android.app.Service
import android.content.Context

/**
 * Check if the service is running in the device.
 *
 * @param serviceClass the service class
 * @return whether the service is running
 */
@Suppress("DEPRECATION")
fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    try {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) return true
        }
    } catch (e: Exception) {
        er { e }
    }
    return false
}

inline fun <reified T : Service> Context.isServiceRunning() = isServiceRunning(T::class.java)