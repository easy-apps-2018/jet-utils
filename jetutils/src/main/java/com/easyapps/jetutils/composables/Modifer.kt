package com.easyapps.jetutils.composables

import android.os.*
import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import kotlin.math.*


@Composable
fun Modifier.onVerticalScroll(scrollState: ScrollState = rememberScrollState()): Modifier {
    val modifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        Modifier.onVerticalFadingEdge(scrollState)
    else
        Modifier.verticalScroll(scrollState)
    return this.then(modifier)
}

fun Modifier.onHorizontalScroll(scrollState: ScrollState): Modifier {
    val modifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        Modifier.onHorizontalFadingEdge(scrollState)
    else
        Modifier.horizontalScroll(scrollState)
    return this.then(modifier)
}

private fun Modifier.onVerticalFadingEdge(scrollState: ScrollState): Modifier {
    val edgeHeight = 50.dp
    return this.then(
        Modifier
            .verticalScroll(scrollState)
            .graphicsLayer { alpha = 0.99F }
            .drawWithContent {
                drawContent()

                val topStartY = scrollState.value.toFloat()
                val topGradientHeight = min(edgeHeight.toPx(), topStartY)
                drawRect(
                    brush = Brush.verticalGradient(
                        startY = topStartY,
                        endY = topStartY + topGradientHeight,
                        colors = listOf(Color.Transparent, Color.Black)
                    ),
                    blendMode = BlendMode.DstIn
                )

                val bottomColors = listOf(Color.Black, Color.Transparent)
                val bottomEndY = size.height - scrollState.maxValue + scrollState.value
                val bottomGradientHeight =
                    min(edgeHeight.toPx(), scrollState.maxValue.toFloat() - scrollState.value)
                if (bottomGradientHeight != 0f)
                    drawRect(
                        brush = Brush.verticalGradient(
                            endY = bottomEndY,
                            colors = bottomColors,
                            startY = bottomEndY - bottomGradientHeight
                        ),
                        blendMode = BlendMode.DstIn
                    )
            })
}

private fun Modifier.onHorizontalFadingEdge(scrollState: ScrollState): Modifier {
    return this.then(
        Modifier
            .horizontalScroll(scrollState)
            .graphicsLayer { alpha = 0.99F }
            .drawWithContent {
                drawContent()
                val edgeSize = 50.dp.toPx()

                val topStartY = scrollState.value.toFloat()
                val topGradientHeight = min(edgeSize, topStartY)
                drawRect(
                    brush = Brush.horizontalGradient(
                        startX = topStartY,
                        endX = topStartY + topGradientHeight,
                        colors = listOf(Color.Transparent, Color.Black)
                    ),
                    blendMode = BlendMode.DstIn
                )

                val bottomEndY = size.width - scrollState.maxValue + scrollState.value
                val bottomGradientHeight =
                    min(edgeSize, scrollState.maxValue.toFloat() - scrollState.value)
                if (bottomGradientHeight != 0f)
                    drawRect(
                        brush = Brush.horizontalGradient(
                            endX = bottomEndY,
                            startX = bottomEndY - bottomGradientHeight,
                            colors = listOf(Color.Black, Color.Transparent)
                        ),
                        blendMode = BlendMode.DstIn
                    )
            })
}

fun Modifier.onVerticalGridFadingEdge(color: Color, lazyListState: LazyStaggeredGridState): Modifier {
    val length = 85.dp
    return this.then(
        Modifier
            .graphicsLayer { alpha = 0.99F }
            .drawWithContent {
                val topFadingEdgeStrength by derivedStateOf {
                    lazyListState.layoutInfo
                        .run {
                            visibleItemsInfo
                                .firstOrNull()
                                ?.let { firstItem ->
                                    when {
                                        visibleItemsInfo.size in 0..1 -> 0f
                                        firstItem.index > 0 -> 1f // Added
                                        firstItem.offset.y == viewportStartOffset -> 0f
                                        firstItem.offset.y < viewportStartOffset -> firstItem.run {
                                            abs(offset.y) / size.height.toFloat()
                                        }

                                        else -> 1f
                                    }
                                }

                        }
                        ?.coerceAtMost(1f)
                        ?.times(length.value) ?: 0f
                }
                val bottomFadingEdgeStrength by derivedStateOf {
                    lazyListState.layoutInfo
                        .run {
                            visibleItemsInfo
                                .lastOrNull()
                                ?.let { lastItem ->
                                    when {
                                        visibleItemsInfo.size in 0..1 -> 0f
                                        lastItem.index < totalItemsCount - 1 -> 1f // Added
                                        lastItem.offset.y + lastItem.size.height.toFloat() <= viewportEndOffset -> 0f
                                        lastItem.offset.y + lastItem.size.height.toFloat() > viewportEndOffset -> lastItem.run {
                                            (size.height - (viewportEndOffset - offset.y)) / size.height.toFloat()  // Fixed the percentage computation
                                        }

                                        else -> 1f
                                    }
                                }
                        }
                        ?.coerceAtMost(1f)
                        ?.times(length.value) ?: 0f
                }

                drawContent()

                drawRect(
                    brush = Brush.verticalGradient(
                        startY = 0f,
                        endY = topFadingEdgeStrength,
                        colors = listOf(color, Color.Transparent)
                    ),
                    size = Size(width = this.size.width, height = topFadingEdgeStrength),
                )

                drawRect(
                    brush = Brush.verticalGradient(
                        endY = size.height,
                        colors = listOf(Color.Transparent, color),
                        startY = size.height - bottomFadingEdgeStrength
                    ),
                    topLeft = Offset(x = 0f, y = size.height - bottomFadingEdgeStrength)
                )
            })
}