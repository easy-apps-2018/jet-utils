package com.easyapps.jetutils.utils

import android.content.*
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.datastore.core.*
import androidx.datastore.core.IOException
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.*
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class StoreData(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private const val DATA_STORE = "Data store"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE)


        fun onPrefInt(key: String): Preferences.Key<Int> {
            return intPreferencesKey(key)
        }

        fun onPrefString(key: String): Preferences.Key<String> {
            return stringPreferencesKey(key)
        }

        fun onPrefBoolean(key: String): Preferences.Key<Boolean> {
            return booleanPreferencesKey(key)
        }

        fun onPrefFloat(key: String): Preferences.Key<Float> {
            return floatPreferencesKey(key)
        }
    }

    fun clearStore() {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences -> preferences.clear() }
        }
    }

    fun <T> onDataStore(map: Map<String, T?>) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                map.forEach {
                    val key = it.key
                    when (val value = it.value) {
                        is Int -> preferences[onPrefInt(key)] = value
                        is Float -> preferences[onPrefFloat(key)] = value
                        is String -> preferences[onPrefString(key)] = value
                        is Boolean -> preferences[onPrefBoolean(key)] = value
                    }
                }

            }
        }
    }

    fun <T> putData(key: String, value: T) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { preferences ->
                when (value) {
                    is Int -> preferences[onPrefInt(key)] = value
                    is Float -> preferences[onPrefFloat(key)] = value
                    is String -> preferences[onPrefString(key)] = value
                    is Boolean -> preferences[onPrefBoolean(key)] = value
                }
            }
        }
    }

    fun <T> getData(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return dataStore.data.catch { exception ->
            if (exception is IOException)
                emit(emptyPreferences())
            else
                throw exception
        }.map { preferences -> preferences[key] ?: defaultValue }
    }
}

fun <T> Context.onStore(map: Map<String, T?>) {
    StoreData(this).onDataStore(map)
}

fun <T> Context.onStore(key: String?, value: T) {
    StoreData(this).putData(key.toString(), value)
}

fun Context.onLiveBoolean(key: String, onObserve: (value: Boolean) -> Unit) {
    StoreData(this).getData(StoreData.onPrefBoolean(key), false).asLiveData()
        .observeForever { onObserve.invoke(it) }
}

fun Context.onLiveBoolean(key: String): LiveData<Boolean> {
    return StoreData(this).getData(StoreData.onPrefBoolean(key), false).asLiveData()
}

fun Context.onLiveString(key: String, onObserve: (value: String) -> Unit) {
    StoreData(this).getData(StoreData.onPrefString(key), EMPTY).asLiveData()
        .observeForever { onObserve.invoke(it) }
}

fun Context.onLiveString(key: String): LiveData<String> {
    return StoreData(this).getData(StoreData.onPrefString(key), EMPTY).asLiveData()
}

@Composable
fun Context.onStateFloat(key: String, default: Float = 0f): State<Float> {
    return StoreData(this).getData(StoreData.onPrefFloat(key), default).collectAsState(default)
}

@Composable
fun Context.onStateBoolean(key: String, default: Boolean = false): State<Boolean> {
    return StoreData(this).getData(StoreData.onPrefBoolean(key), default).collectAsState(default)
}

@Composable
fun Context.onStateString(key: String, default: String = EMPTY): State<String> {
    return StoreData(this).getData(StoreData.onPrefString(key), default).collectAsState(default)
}

@Composable
fun Context.onStateInt(key: String, default: Int = 0): State<Int> {
    return StoreData(this).getData(StoreData.onPrefInt(key), default).collectAsState(default)
}