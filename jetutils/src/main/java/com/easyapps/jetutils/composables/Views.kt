package com.easyapps.jetutils.composables

import androidx.activity.compose.*
import androidx.annotation.*
import androidx.compose.animation.*
import androidx.compose.animation.graphics.*
import androidx.compose.animation.graphics.res.*
import androidx.compose.animation.graphics.vector.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import com.easyapps.jetutils.*
import com.easyapps.jetutils.R
import com.easyapps.jetutils.utils.*
import kotlinx.coroutines.*
import kotlin.math.*

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationGraphicsApi::class)
fun Icon(
    modifier: Modifier = Modifier,
    iconSize: Dp = 30.dp,
    @DrawableRes icon: Int,
    visible: Boolean = true,
    checked: Boolean? = null,
    @StringRes contentDescription: Int? = null,
    tint: Color = LocalContentColor.current
) {
    ScaleVisible(visible = visible, modifier = modifier) {
        if (contentDescription != null)
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
                        contentDescription = onString(id = contentDescription)
                    )
                },
                state = rememberTooltipState(),
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()
            )
        else
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
                contentDescription = null
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
    headerSpace: Dp = 70.dp,
    drawerState: DrawerState,
    gesturesEnabled: Boolean = true,
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    drawerContent: @Composable ColumnScope.() -> Unit,
    navigationRailContent: @Composable ColumnScope.() -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
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
    indicatorColor: Color = MaterialTheme.colorScheme.secondary,
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
    fontSize: TextUnit = 16.sp,
    selectedIconColor: Color = MaterialTheme.colorScheme.secondary,
    unselectedContainerColor: Color = MaterialTheme.colorScheme.background,
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

@Composable
fun RadioButton(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    iconSize: Dp = 26.dp,
    fontSize: TextUnit = 16.sp,
    @DrawableRes animatedIcon: Int = R.drawable.ic_radio_button,
    onClick: () -> Unit
) {
    val color = if (selected)
        MaterialTheme.colorScheme.secondary
    else
        LocalContentColor.current

    Card(
        content = {
            Row(
                content = {
                    Icon(
                        checked = selected,
                        icon = animatedIcon,
                        iconSize = iconSize,
                        tint = onAnimateColor(color = color)
                    )
                    Text(
                        text = text,
                        fontSize = fontSize,
                        modifier = Modifier.weight(weight = 1f)
                    )
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            )
        },
        onClick = onClick,
        enabled = !selected,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            disabledContentColor = LocalContentColor.current,
            containerColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomModalSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    dragHandleColor: Color = MaterialTheme.colorScheme.secondary,
    contentScope: @Composable ColumnScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    val bottom = rememberModalBottomSheetState()

    BackHandler(enabled = visible) {
        scope.launch { bottom.hide() }
    }

    if (visible)
        ModalBottomSheet(
            content = {
                Column(
                    content = contentScope,
                    horizontalAlignment = Alignment.CenterHorizontally
                )

            },
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    color = onAnimateColor(color = dragHandleColor)
                )
            },
            sheetState = bottom,
            onDismissRequest = onDismiss,
            modifier = Modifier.navigationBarsPadding(),
            containerColor = MaterialTheme.colorScheme.background
        )

    LaunchedEffect(key1 = visible) {
        if (visible)
            bottom.partialExpand()
        else
            bottom.hide()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AlertDialog(
    visible: Boolean,
    duration: Int = 100,
    onDismiss: () -> Unit,
    horizontal: Dp = 6.dp,
    @StringRes title: Int?,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    content: @Composable ColumnScope.(focusManager: FocusManager) -> Unit
) {
    ScaleVisible(
        content = {
            BasicAlertDialog(
                content = {
                    val focusManager = LocalFocusManager.current

                    Surface(
                        content = {
                            Column(
                                content = {
                                    Text(
                                        text = stringResource(id = title ?: R.string.empty),
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier
                                            .padding(top = 14.dp)
                                            .fillMaxWidth()
                                    )
                                    content.invoke(this, focusManager)
                                },
                                modifier = Modifier.animateContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            )
                        },
                        color = backgroundColor,
                        shape = RoundedCornerShape(size = 12.dp),
                        modifier = Modifier.wrapContentHeight().padding(horizontal = 26.dp)
                    )
                },
                onDismissRequest = onDismiss,
                properties = DialogProperties(usePlatformDefaultWidth = false),
                modifier = Modifier.fillMaxWidth().padding(horizontal = horizontal)
            )
        },
        visible = visible,
        duration = duration
    )
}

@Composable
fun DialogButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @StringRes title: Int,
    textUnit: TextUnit = 16.sp,
    contentColor: Color = Color.White,
    containerColor: Color = MaterialTheme.colorScheme.secondary
) {
    Button(
        content = {
            Text(
                maxLines = 1,
                fontSize = textUnit,
                text = onString(id = title),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Light
            )
        },
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            contentColor = contentColor,
            containerColor = containerColor
        )
    )
}

@Composable
fun HorizontalPager(
    state: PagerState,
    modifier: Modifier,
    maxOffset: Dp = 40.dp,
    userScrollEnabled: Boolean = true,
    content: @Composable ColumnScope.(page: Int) -> Unit
) {

    val currentIndex = state.currentPage
    val currentPageOffset = state.currentPageOffsetFraction

    HorizontalPager(
        state = state,
        pageContent = { page ->
            val offset = maxOffset * when (page) {
                currentIndex -> currentPageOffset.absoluteValue
                currentIndex - 1 -> 1 + currentPageOffset.coerceAtMost(0f)
                currentIndex + 1 -> 1 - currentPageOffset.coerceAtLeast(0f)
                else -> 1f
            }

            Column(
                content = {
                    content.invoke(this, page)
                },
                modifier = Modifier.offset(y = offset).padding(horizontal = 6.dp).onVerticalScroll(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )
        },
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        userScrollEnabled = userScrollEnabled
    )
}

@Composable
fun AnswersCard(
    answer: String,
    correct: String,
    fontSize: TextUnit,
    correctColor: Color,
    answers: List<String>,
    incorrectColor: Color,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    onCLick: (String) -> Unit
) {
    answers.forEach { which ->
        val result = when {
            answer.isEmpty() -> null
            answer == which -> correct == which
            answer != correct && which == correct -> true
            else -> null
        }

        AnswerCard(
            text = which,
            state = result,
            fontSize = fontSize,
            enabled = answer.isEmpty(),
            correctColor = correctColor,
            incorrectColor = incorrectColor,
            backgroundColor = backgroundColor,
            onCLick = { onCLick.invoke(which) }
        )
    }
}

@Composable
private fun AnswerCard(
    text: String,
    state: Boolean?,
    enabled: Boolean,
    fontSize: TextUnit,
    correctColor: Color,
    incorrectColor: Color,
    backgroundColor: Color,
    onCLick: () -> Unit
) {
    val backColor: Color
    val textColor: Color
    val backAlpha: Float
    val strokeColor: Color

    if (state != null) {
        strokeColor = if (state)
            correctColor
        else
            incorrectColor
        textColor = strokeColor
        backColor = strokeColor
        backAlpha = 0.08f
    } else {
        backAlpha = 0.03f
        backColor = backgroundColor
        strokeColor = Color.LightGray
        textColor = LocalContentColor.current
    }

    val back = onAnimateColor(
        color = backColor,
        alpha = backAlpha
    ).compositeOver(background = backgroundColor)

    OutlinedCard(
        content = {
            Text(
                text = text,
                fontSize = (fontSize.value - 3).sp,
                color = onAnimateColor(color = textColor),
                modifier = Modifier.fillMaxWidth().padding(all = 10.dp)
            )
        },
        onClick = onCLick,
        enabled = enabled,
        colors = CardDefaults.outlinedCardColors(
            containerColor = back,
            disabledContainerColor = back
        ),
        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
        border = BorderStroke(width = 1.dp, color = onAnimateColor(color = strokeColor))
    )
}