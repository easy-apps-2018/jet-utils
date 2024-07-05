package com.easyapps.jetutils

import android.os.*

val currentTime: Long
    get() = System.currentTimeMillis()

fun onHandler(delay: Long, onPostDelayed: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({ onPostDelayed.invoke() }, delay)
}