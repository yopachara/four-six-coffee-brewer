package com.yopachara.fourtosixmethod.feature.timer.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.designsystem.component.GlassCard
import com.yopachara.fourtosixmethod.feature.timer.component.BalanceDisplay
import com.yopachara.fourtosixmethod.feature.timer.component.BodyDisplay
import com.yopachara.fourtosixmethod.feature.timer.component.IcedDripDisplay
import com.yopachara.fourtosixmethod.feature.timer.component.RatioDisplay
import com.yopachara.fourtosixmethod.feature.timer.component.RecipeSettingBottomSheet
import com.yopachara.fourtosixmethod.feature.timer.component.StepsDisplay
import com.yopachara.fourtosixmethod.feature.timer.component.TimerDisplay
import com.yopachara.fourtosixmethod.feature.timer.component.WeightDisplay
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import com.yopachara.fourtosixmethod.feature.timer.viewmodel.TimerViewModel

@Composable
internal fun TimerRoute(
    modifier: Modifier = Modifier,
    viewModel: TimerViewModel = hiltViewModel(),
) {
    val timerState by viewModel.timerDisplayStateFlow.collectAsStateWithLifecycle()
    val stepsDefaultExpanded by viewModel.stepsDefaultExpanded.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* no-op: timer still runs, notification just won't show without this */ }

    TimerScreen(
        timerDisplayState = timerState,
        toggleStartPause = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
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

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            TimerBottomBar(
                timerDisplayState = timerDisplayState,
                onToggle = toggleStartPause,
                onStop = onStop,
            )
        }
    ) { contentPadding ->
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

@Composable
private fun TimerHeader(
    recipe: Recipe,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 4.dp),
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

@Composable
private fun IcePrepBanner(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
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
    val isRunning = timerDisplayState.isRunning()
    val isPlaying = timerDisplayState.isPlaying()

    GlassCard(
        blur = 8.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
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
                        .height(48.dp)
                        .clip(RoundedCornerShape(16.dp))
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
    }

}
