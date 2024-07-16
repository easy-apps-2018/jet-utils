package com.easyapps.jetutils.utils

import android.content.*
import android.os.*
import androidx.activity.ComponentActivity

val currentTime: Long
    get() = System.currentTimeMillis()

fun onHandler(delay: Long, onPostDelayed: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({ onPostDelayed.invoke() }, delay)
}