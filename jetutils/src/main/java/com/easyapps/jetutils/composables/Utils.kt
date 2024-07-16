package com.easyapps.jetutils.composables

import androidx.activity.*
import androidx.annotation.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.runtime.snapshots.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import com.easyapps.jetutils.R
import com.easyapps.jetutils.utils.*

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun ComponentActivity.CalculateWindowSizeClass(
    onMedium: (Boolean) -> Unit = {},
    onCompact: (Boolean) -> Unit = {},
    onExpanded: (Boolean) -> Unit = {}
) {
    val windowSize = calculateWindowSizeClass(this)
    onCompact(windowSize.widthSizeClass == WindowWidthSizeClass.Compact || windowSize.heightSizeClass == WindowHeightSizeClass.Compact)
    onMedium(windowSize.widthSizeClass == WindowWidthSizeClass.Medium || windowSize.heightSizeClass == WindowHeightSizeClass.Medium)
    onExpanded(windowSize.widthSizeClass == WindowWidthSizeClass.Expanded || windowSize.heightSizeClass == WindowHeightSizeClass.Expanded)
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
fun <T : Any> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
    return rememberSaveable(
        saver = listSaver(
            save = { stateList -> stateList.toList() },
            restore = { list -> list.toMutableStateList() }
        )
    ) {
        elements.toList().toMutableStateList()
    }
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
fun onPlural(@PluralsRes res: Int?, count: Int, vararg formatArgs: Any): String {
    return pluralStringResource(id = res ?: R.string.empty, count = count, formatArgs = formatArgs)
}

@Composable
fun onString(@StringRes id: Int): String {
    return stringResource(id = id)
}

@Composable
fun onString(@StringRes id: Int?, vararg formatArgs: Any): String {
    return stringResource(id = id ?: R.string.empty, formatArgs = formatArgs)
}