package com.diplom.diplom_pdr.presentation.utils

import android.content.Context
import android.os.PowerManager

class AppPowerManager(private val context: Context) {
    private var _wakeLock : PowerManager.WakeLock? = null
    private val wakeLock: PowerManager.WakeLock
        get() = _wakeLock ?: throw NullPointerException("Сначала дайте команду не спать")

    fun stayWake() {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        _wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "myapp:wakeLogTag")

        wakeLock.acquire()
    }

    fun releaseWake() {
        wakeLock.release()
    }
}