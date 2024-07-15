package com.easyapps.jetutils

import android.os.*
import androidx.annotation.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.runtime.snapshots.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*

val currentTime: Long
    get() = System.currentTimeMillis()

fun onHandler(delay: Long, onPostDelayed: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({ onPostDelayed.invoke() }, delay)
}

@Composable
fun onAnimateColor(color: Color, duration: Int = 300): Color {
    val state by animateColorAsState(
        label = EMPTY,
        targetValue = color,
        animationSpec = tween(duration)
    )
    return state.copy()
}

@Composable
fun <S> AnimatedContent(
    state: S,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedContentScope.(targetState: S) -> Unit
) {
    AnimatedContent(
        label = EMPTY,
        content = content,
        modifier = modifier,
        targetState = state
    )
}

@Composable
fun SlideOutVisible(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        content = content,
        visible = visible,
        modifier = modifier,
        exit = slideOutHorizontally(),
        enter = slideInHorizontally()
    )
}

@Composable
fun SlideDownVisible(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        content = content,
        visible = visible,
        modifier = modifier,
        exit = slideOutVertically(),
        enter = slideInVertically()
    )
}

@Composable
fun SlideInVisible(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        content = content,
        visible = visible,
        modifier = modifier,
        exit = slideOutVertically(targetOffsetY = { it / 2 }),
        enter = slideInVertically(initialOffsetY = { it / 2 })
    )
}


@Composable
fun ScaleVisible(
    visible: Boolean,
    modifier: Modifier = Modifier,
    duration: Int = 300,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        content = content,
        visible = visible,
        modifier = modifier,
        enter = scaleIn(tween(duration)),
        exit = scaleOut(tween(duration))
    )
}

@Composable
fun onPlural(@PluralsRes res: Int?, count: Int): String {
    return pluralStringResource(res ?: R.string.empty, count, count)
}

@Composable
fun onString(@StringRes id: Int): String {
    return stringResource(id = id)
}

@Composable
fun <T: Any> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
    return rememberSaveable(saver = snapshotStateListSaver()) {
        elements.toList().toMutableStateList()
    }
}

private fun <T : Any> snapshotStateListSaver() = listSaver<SnapshotStateList<T>, T>(
    save = { stateList -> stateList.toList() },
    restore = { it.toMutableStateList() },
)