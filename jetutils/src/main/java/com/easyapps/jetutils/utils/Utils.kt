package com.easyapps.jetutils.utils

import android.os.*
import androidx.lifecycle.*
import kotlinx.coroutines.*

val currentTime: Long
    get() = System.currentTimeMillis()

fun onHandler(delay: Long, onPostDelayed: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({ onPostDelayed.invoke() }, delay)
}

fun AndroidViewModel.onLaunch(block: suspend CoroutineScope.() -> Unit) {
    this.viewModelScope.launch { withContext(Dispatchers.IO) { block.invoke(this) } }
}