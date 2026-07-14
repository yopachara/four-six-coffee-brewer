package com.yopachara.fourtosixmethod.feature.timer.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.data.model.displayName
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState

@Composable
fun TimerDisplay(
    timerDisplayState: TimerDisplayState,
    toggleStartPause: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isTimerRunning = timerDisplayState.secondsRemaining != null

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TimerStateMessageCard(
            timerDisplayState = timerDisplayState,
            isTimerRunning = isTimerRunning
        )

        TimerReadout(
            timerDisplayState = timerDisplayState,
            onToggle = toggleStartPause
        )

        TimerProgressCard(
            timerDisplayState = timerDisplayState,
            isTimerRunning = isTimerRunning
        )

        TimerParameterGrid(recipe = timerDisplayState.recipe)

        TimerActionButtons(
            isPlaying = timerDisplayState.isPlaying(),
            isStopEnabled = isTimerRunning,
            onToggle = toggleStartPause,
            onStop = onStop
        )
    }
}

@Composable
private fun TimerStateMessageCard(
    timerDisplayState: TimerDisplayState,
    isTimerRunning: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isTimerRunning) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        val stateText = when {
            !isTimerRunning -> "Ready to brew? Press play below to start."
            timerDisplayState.isPlaying() -> {
                val currentIndex = timerDisplayState.getCurrentStateIndex()
                val targetWater = timerDisplayState.getWaterWeightCurrentState()
                when (currentIndex) {
                    0 -> "Step 1: Pour $targetWater g water to Bloom 🌸"
                    1 -> "Step 2: Pour $targetWater g water for sweetness & acidity ⚖️"
                    else -> "Step ${currentIndex + 1}: Pour $targetWater g water for strength 💪"
                }
            }
            else -> "Brewing is Paused..."
        }
        Text(
            text = stateText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TimerReadout(
    timerDisplayState: TimerDisplayState,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable { onToggle() }
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(vertical = 28.dp, horizontal = 16.dp)
    ) {
        Text(
            text = timerDisplayState.displaySeconds,
            fontSize = 76.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Text(
            text = if (timerDisplayState.isPlaying()) "TAP TO PAUSE" else "TAP TO START",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun ProgressRow(
    label: String,
    valueLabel: String,
    valueColor: androidx.compose.ui.graphics.Color,
    progress: Float,
    progressColor: androidx.compose.ui.graphics.Color,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valueLabel,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth(),
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            color = progressColor,
            strokeCap = StrokeCap.Round
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TimerProgressCard(
    timerDisplayState: TimerDisplayState,
    isTimerRunning: Boolean,
    modifier: Modifier = Modifier
) {
    val totalAnimateProgress = animateFloatAsState(
        targetValue = timerDisplayState.progressPercentage,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "totalAnimateProgress"
    ).value

    val stateAnimateProgress = animateFloatAsState(
        targetValue = timerDisplayState.statePercentage,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "stateAnimateProgress"
    ).value

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProgressRow(
                label = "Step progress",
                valueLabel = "${timerDisplayState.getCurrentStateMaxTime()} s limit",
                valueColor = MaterialTheme.colorScheme.secondary,
                progress = if (isTimerRunning) stateAnimateProgress else 0.0f,
                progressColor = MaterialTheme.colorScheme.secondary
            )

            ProgressRow(
                label = "Overall progress",
                valueLabel = "${timerDisplayState.totalSeconds} s total",
                valueColor = MaterialTheme.colorScheme.primary,
                progress = if (isTimerRunning) (1 - totalAnimateProgress) else 0.0f,
                progressColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ParameterCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        modifier = modifier
            .requiredHeight(80.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TimerParameterGrid(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        maxItemsInEachRow = 2,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ParameterCard(
            title = "Coffee weight",
            value = "${recipe.coffeeWeight} g",
            modifier = Modifier.weight(1f)
        )
        ParameterCard(
            title = "Ratio (yield)",
            value = "1:${recipe.ratio} (${recipe.getTotalWater()}g)",
            modifier = Modifier.weight(1f)
        )
        ParameterCard(
            title = "Balance Profile",
            value = recipe.balance.name,
            modifier = Modifier.weight(1f)
        )
        ParameterCard(
            title = "Mouthfeel Strength",
            value = recipe.level.displayName,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TimerActionButtons(
    isPlaying: Boolean,
    isStopEnabled: Boolean,
    onToggle: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilledIconButton(
            onClick = onToggle,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        ) {
            if (isPlaying) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = "Pause brew",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start brew",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        FilledIconButton(
            onClick = onStop,
            enabled = isStopEnabled,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = "Stop brew",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimerDisplay() {
    TimerDisplay(timerDisplayState = TimerDisplayState(60), {}, {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimerStateMessageCard() {
    TimerStateMessageCard(
        timerDisplayState = TimerDisplayState(60),
        isTimerRunning = true
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimerReadout() {
    TimerReadout(timerDisplayState = TimerDisplayState(60), onToggle = {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimerProgressCard() {
    TimerProgressCard(timerDisplayState = TimerDisplayState(60), isTimerRunning = true)
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimerParameterGrid() {
    TimerParameterGrid(recipe = Recipe())
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimerActionButtons() {
    TimerActionButtons(
        isPlaying = false,
        isStopEnabled = true,
        onToggle = {},
        onStop = {}
    )
}