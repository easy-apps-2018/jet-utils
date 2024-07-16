package com.easyapps.jetutils.utils

import android.content.*
import android.content.pm.*
import android.net.*
import android.widget.*
import androidx.activity.*
import androidx.annotation.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.*

fun Context.onToast(@StringRes text: Int?) {
    if (text != null)
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.onShare(@StringRes appName: Int) {
    val string =
        this.resources.getString(appName) + ": https://play.google.com/store/apps/details?id=${this.packageName}"
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, string)
        type = "text/plain"
    }
    this.startActivity(Intent.createChooser(sendIntent, null))
}

fun Context.onString(@StringRes res: Int): String {
    return this.resources.getString(res)
}

fun Context.onFeedback(@StringRes message: Int) {
    val email = Intent(Intent.ACTION_SEND).apply {
        type = "text/email"
        putExtra(Intent.EXTRA_EMAIL, arrayOf("easy.app.2018@gmail.com"))
        putExtra(Intent.EXTRA_SUBJECT, this@onFeedback.onString(message))
        putExtra(Intent.EXTRA_TEXT, EMPTY)
    }
    try {
        this.startActivity(
            Intent.createChooser(email, this@onFeedback.onString(message) + ":")
        )
    } catch (_: ActivityNotFoundException) { }
}

fun Context.onPlay() {
    try {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=${this.packageName}")
            )
        )
    } catch (e: ActivityNotFoundException) {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=${this.packageName}")
            )
        )
    }
}

fun Context.onAssets(name: String): String {
    return this.assets.open(name).bufferedReader().use { it.readText() }
}

fun Context.onExit() {
    (this as ComponentActivity).moveTaskToBack(true)
}

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