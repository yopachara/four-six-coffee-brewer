package com.yopachara.fourtosixmethod.feature.timer.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import kotlinx.coroutines.delay

@Composable
fun TimerDisplay(
    timerDisplayState: TimerDisplayState,
    toggleStartPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        TimerReadout(
            timerDisplayState = timerDisplayState,
            onToggle = toggleStartPause,
            modifier = Modifier.fillMaxWidth()
        )

        StepProgressBar(
            timerDisplayState = timerDisplayState,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private val loopDotsFrames =
    listOf('․', '‥', '⁚', '…', '⁖', '⁘', '⁙', '⁛', '⁙', '⁘', '⁖', '…', '⁚', '‥')

@Composable
private fun LoopingDotsIndicator(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    frameDurationMillis: Long = 300L
) {
    var frameIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(isPlaying) {
        if (!isPlaying) {
            frameIndex = 0
            return@LaunchedEffect
        }
        while (true) {
            delay(frameDurationMillis)
            frameIndex = (frameIndex + 1) % loopDotsFrames.size
        }
    }

    Text(
        text = loopDotsFrames[frameIndex].toString(),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
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
            .clickable { onToggle() }
            .padding(top = 32.dp, bottom = 24.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = timerDisplayState.displaySeconds,
                fontSize = 76.sp,
                lineHeight = 76.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-3).sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = "/ ${timerDisplayState.displayTotalSeconds}",
                fontSize = 18.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.alignByBaseline()
            )
        }
        Text(
            text = if (timerDisplayState.isPlaying()) "TAP TO PAUSE" else "TAP TO START",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Composable
private fun StepProgressBar(
    timerDisplayState: TimerDisplayState,
    modifier: Modifier = Modifier
) {
    val isRunning = timerDisplayState.isRunning()
    val currentIndex = timerDisplayState.getCurrentStateIndex()
    val steps = timerDisplayState.recipe.steps

    Column(modifier = modifier) {
        Row(modifier = Modifier.height(32.dp), verticalAlignment = Alignment.CenterVertically) {
            LoopingDotsIndicator(
                isPlaying = timerDisplayState.isPlaying(),
                modifier = Modifier
                    .width(20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = timerDisplayState.stateMessage(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = timerDisplayState.stepTimeRemainingLabel(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            steps.forEachIndexed { index, step ->
                val targetFill = when {
                    isRunning && index < currentIndex -> 1f
                    isRunning && index == currentIndex -> timerDisplayState.statePercentage
                    else -> 0f
                }
                val fill by animateFloatAsState(
                    targetValue = targetFill.coerceIn(0f, 1f),
                    animationSpec = tween(durationMillis = 900, easing = LinearEasing),
                    label = "stepFill$index"
                )
                Box(
                    modifier = Modifier
                        .weight(step.time.toFloat())
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    if (fill > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fill)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimerDisplay() {
    TimerDisplay(timerDisplayState = TimerDisplayState(60), toggleStartPause = {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimerReadout() {
    TimerReadout(timerDisplayState = TimerDisplayState(60), onToggle = {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewStepProgressBar() {
    StepProgressBar(timerDisplayState = TimerDisplayState(60))
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoopingDotsIndicator() {
    LoopingDotsIndicator(isPlaying = true)
}
