package com.yopachara.fourtosixmethod.core.designsystem.layout

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Material's width buckets: phone portrait, phone landscape / small tablet, large tablet.
 */
enum class WindowWidthClass { Compact, Medium, Expanded }

/**
 * Material's height buckets. [Compact] is the one that matters here - a phone held in landscape
 * has plenty of width but not enough height for a 128sp timer plus a pour schedule.
 */
enum class WindowHeightClass { Compact, Medium, Expanded }

@Immutable
data class WindowSizeClass(
    val width: WindowWidthClass,
    val height: WindowHeightClass,
    val widthDp: Dp,
    val heightDp: Dp,
) {
    /** Two-pane territory: navigation moves to a rail and screens may split into panes. */
    val isWide: Boolean get() = width != WindowWidthClass.Compact

    /** Only a large tablet has the width to spare for text labels next to the rail icons. */
    val showNavigationRailLabels: Boolean
        get() = width == WindowWidthClass.Expanded && height != WindowHeightClass.Compact

    companion object {
        fun fromSize(widthDp: Dp, heightDp: Dp): WindowSizeClass = WindowSizeClass(
            width = when {
                widthDp < 600.dp -> WindowWidthClass.Compact
                widthDp < 840.dp -> WindowWidthClass.Medium
                else -> WindowWidthClass.Expanded
            },
            height = when {
                heightDp < 480.dp -> WindowHeightClass.Compact
                heightDp < 900.dp -> WindowHeightClass.Medium
                else -> WindowHeightClass.Expanded
            },
            widthDp = widthDp,
            heightDp = heightDp,
        )
    }
}

/**
 * Defaults to a phone portrait so a composable previewed or tested outside [ProvideWindowSizeClass]
 * still renders the layout it was written for.
 */
val LocalWindowSizeClass = staticCompositionLocalOf {
    WindowSizeClass.fromSize(widthDp = 412.dp, heightDp = 892.dp)
}

/**
 * Measures the window once, near the root, and publishes the result to every screen below.
 *
 * `BoxWithConstraints` rather than a platform window-metrics API so the whole thing stays in
 * `commonMain`; it also means a resized desktop-class window or a foldable unfolding re-measures
 * for free.
 */
@Composable
fun ProvideWindowSizeClass(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        val sizeClass = WindowSizeClass.fromSize(widthDp = maxWidth, heightDp = maxHeight)
        CompositionLocalProvider(LocalWindowSizeClass provides sizeClass) {
            content()
        }
    }
}
