package com.easyapps.jetutils.utils

import android.os.*
import androidx.compose.foundation.pager.*
import androidx.lifecycle.*
import kotlinx.coroutines.*

val currentTime: Long
    get() = System.currentTimeMillis()

fun onHandler(delay: Long, onPostDelayed: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({ onPostDelayed.invoke() }, delay)
}