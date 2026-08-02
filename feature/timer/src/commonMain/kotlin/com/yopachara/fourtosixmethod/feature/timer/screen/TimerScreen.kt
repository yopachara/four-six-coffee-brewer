package com.yopachara.fourtosixmethod.feature.timer.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.designsystem.layout.LocalWindowSizeClass
import com.yopachara.fourtosixmethod.core.designsystem.layout.WindowHeightClass
import com.yopachara.fourtosixmethod.core.designsystem.layout.WindowWidthClass
import com.yopachara.fourtosixmethod.feature.timer.component.GlassCard
import com.yopachara.fourtosixmethod.feature.timer.component.RecipeSettingBottomSheet
import com.yopachara.fourtosixmethod.feature.timer.component.StepProgressBar
import com.yopachara.fourtosixmethod.feature.timer.component.StepsDisplay
import com.yopachara.fourtosixmethod.feature.timer.component.StepsSchedulePanel
import com.yopachara.fourtosixmethod.feature.timer.component.TimerDisplay
import com.yopachara.fourtosixmethod.feature.timer.component.TimerReadout
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import com.yopachara.fourtosixmethod.feature.timer.viewmodel.TimerViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun TimerRoute(
    modifier: Modifier = Modifier,
    viewModel: TimerViewModel = koinViewModel(),
) {
    val timerState by viewModel.timerDisplayStateFlow.collectAsStateWithLifecycle()
    val stepsDefaultExpanded by viewModel.stepsDefaultExpanded.collectAsStateWithLifecycle()
    val requestNotificationPermission = rememberNotificationPermissionRequester()

    TimerScreen(
        timerDisplayState = timerState,
        toggleStartPause = {
            requestNotificationPermission()
            viewModel.toggleTime()
        },
        onStop = viewModel::stopTime,
        onWeightChanged = viewModel::setCoffeeWeight,
        onRatioChanged = viewModel::setCoffeeRatio,
        onBalanceChange = viewModel::setCoffeeBalance,
        onBodyChange = viewModel::setCoffeeLevel,
        onIcedDripToggle = viewModel::setIcedDrip,
        onHotRatioChange = viewModel::setHotRatio,
        stepsDefaultExpanded = stepsDefaultExpanded,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimerScreen(
    timerDisplayState: TimerDisplayState,
    toggleStartPause: () -> Unit,
    onStop: () -> Unit,
    onWeightChanged: (Float) -> Unit,
    onRatioChanged: (Int) -> Unit,
    onBalanceChange: (Balance) -> Unit,
    onBodyChange: (Level) -> Unit,
    onIcedDripToggle: (Boolean) -> Unit,
    onHotRatioChange: (Int) -> Unit,
    stepsDefaultExpanded: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val recipe = timerDisplayState.recipe
    val isRunning = timerDisplayState.isRunning()

    val windowSizeClass = LocalWindowSizeClass.current

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            // A wide window puts the transport controls at the foot of the timer pane instead,
            // so the schedule pane runs the full height beside them.
            if (!windowSizeClass.isWide) {
                TimerBottomBar(
                    timerDisplayState = timerDisplayState,
                    onToggle = toggleStartPause,
                    onStop = onStop,
                )
            }
        }
    ) { contentPadding ->
        if (windowSizeClass.isWide) {
            WideTimerContent(
                timerDisplayState = timerDisplayState,
                toggleStartPause = toggleStartPause,
                onStop = onStop,
                onOpenSettings = { showBottomSheet = true },
                modifier = Modifier.padding(contentPadding)
            )
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(contentPadding)
            ) {
                TimerHeader(
                    recipe = recipe,
                    onOpenSettings = { showBottomSheet = true }
                )

                if (recipe.isIcedDrip && !isRunning) {
                    IcePrepBanner(recipe = recipe)
                }

                TimerDisplay(
                    timerDisplayState = timerDisplayState,
                    toggleStartPause = toggleStartPause
                )

                StepsDisplay(
                    timerDisplayState = timerDisplayState,
                    defaultExpanded = stepsDefaultExpanded
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (showBottomSheet) {
            RecipeSettingBottomSheet(
                timerDisplayState = timerDisplayState,
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                onWeightChanged = onWeightChanged,
                onRatioChanged = onRatioChanged,
                onIcedDripToggle = onIcedDripToggle,
                onHotRatioChange = onHotRatioChange,
                onBalanceChange = onBalanceChange,
                onBodyChange = onBodyChange
            )
        }
    }
}

/**
 * Landscape and tablet layout: navigation already lives in the app-level rail, so the screen
 * splits into a timer pane (header, readout, progress, transport) and a schedule pane that keeps
 * every pour on screen at once.
 */
@Composable
private fun WideTimerContent(
    timerDisplayState: TimerDisplayState,
    toggleStartPause: () -> Unit,
    onStop: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = LocalWindowSizeClass.current
    val isExpandedWidth = windowSizeClass.width == WindowWidthClass.Expanded
    val isShort = windowSizeClass.height == WindowHeightClass.Compact
    val recipe = timerDisplayState.recipe
    val isRunning = timerDisplayState.isRunning()

    // The readout is the one element with room to grow, so it carries the size difference
    // between a phone on its side and a tablet.
    val timerFontSize = when {
        isShort -> 72.sp
        isExpandedWidth -> 128.sp
        else -> 96.sp
    }
    val timerTotalFontSize = when {
        isShort -> 18.sp
        isExpandedWidth -> 26.sp
        else -> 22.sp
    }
    val panePadding = if (isExpandedWidth) 32.dp else 20.dp
    val schedulePaneWidth = if (isExpandedWidth) 380.dp else 320.dp

    Row(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = panePadding, vertical = if (isShort) 12.dp else 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TimerHeader(
                recipe = recipe,
                onOpenSettings = onOpenSettings,
                showSettingsLabel = true,
                contentPadding = PaddingValues()
            )

            if (recipe.isIcedDrip && !isRunning) {
                IcePrepBanner(recipe = recipe, contentPadding = PaddingValues())
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TimerReadout(
                    timerDisplayState = timerDisplayState,
                    onToggle = toggleStartPause,
                    fontSize = timerFontSize,
                    totalFontSize = timerTotalFontSize,
                    contentPadding = PaddingValues(vertical = 8.dp)
                )
            }

            StepProgressBar(
                timerDisplayState = timerDisplayState,
                barHeight = 8.dp,
                modifier = Modifier.fillMaxWidth()
            )

            TimerControls(
                timerDisplayState = timerDisplayState,
                onToggle = toggleStartPause,
                onStop = onStop,
                buttonSize = if (isShort) 48.dp else 56.dp,
                modifier = Modifier.padding(top = if (isShort) 4.dp else 12.dp)
            )
        }

        Column(
            modifier = Modifier
                .width(schedulePaneWidth)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = if (isShort) 16.dp else 24.dp)
        ) {
            StepsSchedulePanel(timerDisplayState = timerDisplayState)
        }
    }
}

@Composable
private fun TimerHeader(
    recipe: Recipe,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    showSettingsLabel: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(
        start = 20.dp,
        end = 20.dp,
        top = 16.dp,
        bottom = 4.dp
    )
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "4:6 METHOD",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${recipe.coffeeWeight}g · 1:${recipe.ratio} · ${
                    recipe.getTotalWater().toInt()
                }g water",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        if (recipe.isIcedDrip) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AcUnit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${recipe.getIceWeight().toInt()}g ice",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (showSettingsLabel) {
            // Wide windows have room to spell the action out rather than leaning on the icon.
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .height(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { onOpenSettings() }
                    .padding(horizontal = 14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Recipe",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { onOpenSettings() }
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Recipe settings",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun IcePrepBanner(
    recipe: Recipe,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 4.dp)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AcUnit,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = "Add ${recipe.getIceWeight().toInt()}g ice to the server before you start",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}

@Composable
private fun TimerBottomBar(
    timerDisplayState: TimerDisplayState,
    onToggle: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier.fillMaxWidth()) {
        TimerControls(
            timerDisplayState = timerDisplayState,
            onToggle = onToggle,
            onStop = onStop,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        )
    }
}

@Composable
private fun TimerControls(
    timerDisplayState: TimerDisplayState,
    onToggle: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 48.dp
) {
    val isRunning = timerDisplayState.isRunning()
    val isPlaying = timerDisplayState.isPlaying()
    val shape = RoundedCornerShape(if (buttonSize >= 56.dp) 18.dp else 16.dp)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(buttonSize)
                .clip(shape)
                .background(
                    if (isRunning) MaterialTheme.colorScheme.errorContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .clickable(enabled = isRunning) { onStop() }
        ) {
            Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = "Stop brew",
                tint = if (isRunning) MaterialTheme.colorScheme.onErrorContainer
                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .height(buttonSize)
                .clip(shape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onToggle() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = when {
                    isPlaying -> "Pause"
                    isRunning -> "Resume"
                    else -> "Start brewing"
                },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Default,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
