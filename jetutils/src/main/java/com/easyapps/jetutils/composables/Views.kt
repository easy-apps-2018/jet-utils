package com.easyapps.jetutils.composables

import androidx.annotation.*
import androidx.compose.animation.*
import androidx.compose.animation.graphics.*
import androidx.compose.animation.graphics.res.*
import androidx.compose.animation.graphics.vector.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.easyapps.jetutils.*
import com.easyapps.jetutils.R
import com.easyapps.jetutils.utils.*
import kotlinx.coroutines.*

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

@Composable
fun ScaffoldNavigationDrawer(
    modifier: Modifier,
    isRailVisible: Boolean,
    backgroundColor: Color,
    headerSpace: Dp = 70.dp,
    drawerState: DrawerState,
    gesturesEnabled: Boolean = true,
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    drawerContent: @Composable ColumnScope.() -> Unit,
    navigationRailContent: @Composable ColumnScope.() -> Unit,
    navigationRailHeader: @Composable (ColumnScope.() -> Unit)? = null
) {
    ModalNavigationDrawer(
        content = {
            Row(
                content = {
                    SlideOutVisible(
                        content = {
                            NavigationRail(
                                content = {
                                    Column(
                                        content = navigationRailContent,
                                        modifier = Modifier.onVerticalScroll(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    )
                                },
                                header = navigationRailHeader,
                                containerColor = backgroundColor,
                                modifier = Modifier.fillMaxHeight()
                            )
                        },
                        visible = isRailVisible
                    )

                    Scaffold(
                        content = content,
                        bottomBar = bottomBar,
                        snackbarHost = snackbarHost,
                        containerColor = backgroundColor,
                        modifier = Modifier.weight(weight = 1f),
                        floatingActionButton = floatingActionButton
                    )
                },
                modifier = Modifier.animateContentSize().background(color = backgroundColor)
            )
        },
        modifier = modifier,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp),
                drawerContainerColor = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = Modifier.onVerticalScroll()) {
                    Spacer(modifier = Modifier.size(size = headerSpace))
                    drawerContent(this)
                }
            }
        },
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled
    )
}

@Composable
fun NavRailItem(
    navItem: NavItem,
    selected: Boolean,
    iconSize: Dp = 26.dp,
    indicatorColor: Color,
    onClick: suspend CoroutineScope.() -> Unit
) {
    if (navItem.visible) {
        val scope = rememberCoroutineScope()
        NavigationRailItem(
            icon = {
                Icon(
                    icon = navItem.icon,
                    iconSize = iconSize,
                    contentDescription = navItem.title
                )
            },
            onClick = {
                scope.launch { onClick.invoke(this) }
            },
            selected = selected,
            colors = NavigationRailItemDefaults.colors(
                selectedIconColor = indicatorColor,
                indicatorColor = indicatorColor.copy(alpha = 0.1f)
            )
        )
    }
}

@Composable
fun NavDrawerItem(
    navItem: NavItem,
    selected: Boolean,
    iconSize: Dp = 26.dp,
    selectedIconColor: Color,
    fontSize: TextUnit = 16.sp,
    unselectedContainerColor: Color,
    onClick: suspend CoroutineScope.() -> Unit
) {
    if (navItem.visible) {
        val scope = rememberCoroutineScope()
        NavigationDrawerItem(
            selected = selected,
            onClick = {
                scope.launch { onClick.invoke(this) }
            },
            label = {
                Text(
                    fontSize = fontSize,
                    text = onString(id = navItem.title)
                )
            },
            icon = {
                Icon(
                    icon = navItem.icon,
                    iconSize = iconSize,
                    contentDescription = navItem.title
                )
            },
            modifier = Modifier.padding(horizontal = 8.dp),
            colors = NavigationDrawerItemDefaults.colors(
                selectedIconColor = selectedIconColor,
                unselectedContainerColor = unselectedContainerColor,
                selectedContainerColor = selectedIconColor.copy(alpha = 0.1f)
            )
        )
    }
}