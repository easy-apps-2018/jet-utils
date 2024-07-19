package com.easyapps.jetutils.composables

import android.os.*
import androidx.annotation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.core.graphics.*
import kotlinx.parcelize.*

@Parcelize
data class ChartModel(
    val value: Float,
    val color: String
): Parcelable

@Composable
fun PieChart(
    slices: List<Slice>,
    modifier: Modifier = Modifier
) {

    val pieChartData = PieChartData(slices)
    val transitionProgress = remember(pieChartData.slices) { Animatable(0f) }

    Canvas(modifier = modifier) {
        drawIntoCanvas {
            var startArc = 0f
            pieChartData.slices.forEach { slice ->
                if (slice.value > 0) {
                    val arc = 360.0f * (slice.value.toFloat() * transitionProgress.value) / pieChartData.totalSize
                    drawSlice(drawContext.canvas, size, startArc, arc, slice)
                    startArc += arc
                }
            }
        }
    }

    LaunchedEffect(key1 = pieChartData.slices) {
        transitionProgress.animateTo(1f, TweenSpec(300))
    }
}

@Parcelize
data class PieChartData(val slices: @RawValue List<Slice>) : Parcelable {
    internal val totalSize: Float
        get() {
            var total = 0f
            slices.forEach { total += it.value }
            return total
        }
}

@Parcelize
data class Slice(
    val value: Int,
    val color: String,
    @StringRes val string: Int,
    val visible: Boolean = true
) : Parcelable


private fun drawSlice(canvas: Canvas, area: Size, startAngle: Float, sweepAngle: Float, slice: Slice): Rect {
    val sliceThickness = calculateSectorThickness(area = area)
    val drawableArea = calculateDrawableArea(area = area)

    canvas.drawArc(drawableArea, startAngle, sweepAngle, false, Paint().apply {
        color = slice.color.toColor() ?: Color.Transparent
        strokeWidth = sliceThickness
        isAntiAlias = true
        style = PaintingStyle.Stroke
    })
    return drawableArea
}

private fun calculateSectorThickness(area: Size): Float {
    val minSize = minOf(area.width, area.height)

    return minSize * (25f / 200f)
}

private fun calculateDrawableArea(area: Size): Rect {
    val sliceThicknessOffset = calculateSectorThickness(area = area) / 2f
    val offsetHorizontally = (area.width - area.height) / 2f

    return Rect(
        left = sliceThicknessOffset + offsetHorizontally,
        top = sliceThicknessOffset,
        right = area.width - sliceThicknessOffset - offsetHorizontally,
        bottom = area.height - sliceThicknessOffset
    )
}

fun String?.toColor(): Color? {
    return if (this != null) try {
        Color(this.toColorInt())
    } catch (_: Exception) {
        null
    }
    else
        null
}

fun Color.toStr(): String {
    return String.format("#%06X", 0xFFFFFF and this.toArgb())
}