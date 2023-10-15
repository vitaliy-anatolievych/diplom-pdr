package com.diplom.diplom_pdr.presentation.utils

import android.os.Handler

class CountUpTimer(private val updateInterval: Long, private val callback: (Long) -> Unit) {

    private val handler = Handler()
    private var startTime = 0L
    private var isRunning = false

    private val runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - startTime
                callback(elapsedTime)
                handler.postDelayed(this, updateInterval)
            }
        }
    }

    fun start() {
        if (!isRunning) {
            startTime = System.currentTimeMillis()
            isRunning = true
            handler.post(runnable)
        }
    }

    fun stop() {
        isRunning = false
        handler.removeCallbacks(runnable)
    }
}
