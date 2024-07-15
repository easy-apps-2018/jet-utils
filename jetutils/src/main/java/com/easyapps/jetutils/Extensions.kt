package com.easyapps.jetutils

import androidx.activity.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.*

fun ComponentActivity.enableSplashScreen() {
    var isSplash = true
    onHandler(800) { isSplash = false }
    this.installSplashScreen().setKeepOnScreenCondition { isSplash }
}

fun NavHostController.onDestination(onChanged: (route: String) -> Unit) {
    this.addOnDestinationChangedListener { _, destination, _ ->
        onChanged.invoke(destination.route.toString())
    }
}