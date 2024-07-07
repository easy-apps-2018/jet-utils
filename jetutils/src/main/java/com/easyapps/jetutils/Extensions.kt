package com.easyapps.jetutils

import androidx.navigation.*

fun NavController.onNavigate(route: String) {
    this.navigate(route) {
        popUpTo(this@onNavigate.graph.id) {
            this.inclusive = true
        }
    }
}

fun NavHostController.onDestination(onChanged: (route: String) -> Unit) {
    this.addOnDestinationChangedListener { _, destination, _ ->
        onChanged.invoke(destination.route.toString())
    }
}