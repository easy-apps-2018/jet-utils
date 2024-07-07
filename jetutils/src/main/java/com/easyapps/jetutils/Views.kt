package com.easyapps.jetutils

import androidx.annotation.*
import androidx.compose.animation.graphics.*
import androidx.compose.animation.graphics.res.*
import androidx.compose.animation.graphics.vector.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationGraphicsApi::class)
fun Icon(
    modifier: Modifier = Modifier,
    iconSize: Dp = 30.dp,
    @DrawableRes icon: Int,
    visible: Boolean = true,
    checked: Boolean? = null,
    @StringRes contentDescription: Int,
    tint: Color = LocalContentColor.current
) {
    ScaleVisible(visible = visible, modifier = modifier) {
        TooltipBox(
            tooltip = {
                PlainTooltip(
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Text(
                        fontWeight = FontWeight.Light,
                        text = stringResource(id = contentDescription)
                    )
                }
            },
            content = {
                Icon(
                    tint = tint,
                    painter = if (checked != null)
                        rememberAnimatedVectorPainter(
                            atEnd = checked,
                            animatedImageVector = AnimatedImageVector.animatedVectorResource(id = icon)
                        )
                    else
                        painterResource(id = icon),
                    modifier = Modifier.size(size = iconSize),
                    contentDescription = stringResource(id = contentDescription)
                )
            },
            state = rememberTooltipState(),
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()
        )
    }
}

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    iconSize: Dp = 30.dp,
    @DrawableRes icon: Int,
    enabled: Boolean = true,
    visible: Boolean = true,
    checked: Boolean? = null,
    @StringRes contentDescription: Int,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    ScaleVisible(visible = visible, modifier = modifier) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
        ) {
            Icon(
                tint = tint,
                icon = icon,
                checked = checked,
                iconSize = iconSize,
                contentDescription = contentDescription
            )
        }
    }
}

@Composable
fun SmallFloatingButton(
    visible: Boolean,
    onClick: () -> Unit,
    @StringRes hint: Int,
    @DrawableRes icon: Int
) {
    ScaleVisible(visible = visible) {
        FloatingActionButton(
            content = {
                Icon(icon = icon, contentDescription = hint)
            },
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.sizeIn(minHeight = 46.dp, minWidth = 46.dp)
        )
    }
}

@Composable
fun FloatingButton(
    visible: Boolean,
    onClick: () -> Unit,
    @StringRes hint: Int,
    @DrawableRes icon: Int
) {
    ScaleVisible(visible = visible) {
        FloatingActionButton(
            content = {
                Icon(icon = icon, contentDescription = hint)
            },
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun Title(
    modifier: Modifier,
    @StringRes title: Int,
    visible: Boolean = true
) {
    ScaleVisible(visible = visible, modifier = modifier) {
        Text(
            maxLines = 1,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            overflow = TextOverflow.Ellipsis,
            text = stringResource(id = title),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Divider(
    modifier: Modifier,
    color: Color? = null
) {
    val temp = if (color == LocalContentColor.current)
        Color.Gray
    else
        color
    HorizontalDivider(
        modifier = modifier,
        color = onAnimateColor(color = temp ?: MaterialTheme.colorScheme.surface)
    )
}

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    @DrawableRes icon: Int = R.drawable.ic_back,
    @StringRes contentDescription: Int = R.string.navigate_back,
    onClick: () -> Unit
) {
    IconButton(
        icon = icon,
        visible = visible,
        onClick = onClick,
        modifier = modifier,
        contentDescription = contentDescription
    )
}