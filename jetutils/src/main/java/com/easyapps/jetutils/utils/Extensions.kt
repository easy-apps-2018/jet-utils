package com.easyapps.jetutils.utils

import android.content.pm.*
import androidx.activity.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.*

fun NavController.onNavigate(route: String) {
    this.navigate(route) {
        popUpTo(this@onNavigate.graph.id) {
            this.inclusive = true
        }
    }
}

fun ComponentActivity.onScreenLock(isLock: Boolean) {
    this.requestedOrientation = if (isLock)
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    else
        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}

fun ComponentActivity.enableSplashScreen(delay: Long = 800) {
    var isSplash = true
    onHandler(delay) { isSplash = false }
    this.installSplashScreen().setKeepOnScreenCondition { isSplash }
}

fun NavHostController.onDestination(onChanged: (route: String) -> Unit) {
    this.addOnDestinationChangedListener { _, destination, _ ->
        onChanged.invoke(destination.route.toString())
    }
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.onTarget(): String? {
    return this.targetState.destination.route
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.onInitial(): String? {
    return this.initialState.destination.route
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.onExitDown(): ExitTransition {
    return this.slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Down,
        animationSpec = tween()
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.onEnterUp(): EnterTransition {
    return slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Up,
        animationSpec = tween()
    )
}

fun onExitScaleOut(): ExitTransition {
    return scaleOut(animationSpec = tween())
}

fun onEnterScaleIn(): EnterTransition {
    return scaleIn(animationSpec = tween())
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.onExitLeft(): ExitTransition {
    return slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween()
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.onExitRight(): ExitTransition {
    return slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween()
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.onEnterLeft(): EnterTransition {
    return slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween()
    )
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.onEnterRight(): EnterTransition {
    return slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween()
    )
}