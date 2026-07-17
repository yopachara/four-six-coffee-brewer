package com.yopachara.fourtosixmethod.feature.timer.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState

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
        TimerRing(
            timerDisplayState = timerDisplayState,
            onToggle = toggleStartPause,
            modifier = Modifier.padding(top = 8.dp)
        )

        StepProgressBar(
            timerDisplayState = timerDisplayState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        )
    }
}

@Composable
private fun TimerRing(
    timerDisplayState: TimerDisplayState,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = timerDisplayState.progressPercentage,
        animationSpec = tween(durationMillis = 900, easing = LinearEasing),
        label = "ringProgress"
    )
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val progressColor = MaterialTheme.colorScheme.primary

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(220.dp)
            .clip(CircleShape)
            .clickable { onToggle() }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 10.dp.toPx()
            val diameter = size.minDimension - strokeWidth
            val topLeft = Offset(
                (size.width - diameter) / 2f,
                (size.height - diameter) / 2f
            )
            val arcSize = Size(diameter, diameter)

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * (1f - progress),
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = timerDisplayState.displaySeconds,
                fontSize = 52.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (timerDisplayState.isPlaying()) "TAP TO PAUSE" else "TAP TO START",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun StepProgressBar(
    timerDisplayState: TimerDisplayState,
    modifier: Modifier = Modifier
) {
    val stateProgress by animateFloatAsState(
        targetValue = if (timerDisplayState.isRunning()) timerDisplayState.statePercentage else 0f,
        animationSpec = tween(durationMillis = 900, easing = LinearEasing),
        label = "stateProgress"
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = timerDisplayState.stateMessage(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = timerDisplayState.stepTimeRemainingLabel(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        LinearProgressIndicator(
            progress = stateProgress,
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            strokeCap = StrokeCap.Round
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimerDisplay() {
    TimerDisplay(timerDisplayState = TimerDisplayState(60), toggleStartPause = {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimerRing() {
    TimerRing(timerDisplayState = TimerDisplayState(60), onToggle = {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewStepProgressBar() {
    StepProgressBar(timerDisplayState = TimerDisplayState(60))
}
