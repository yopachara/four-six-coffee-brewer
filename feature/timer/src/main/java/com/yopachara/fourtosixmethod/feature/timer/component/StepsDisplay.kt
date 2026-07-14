package com.yopachara.fourtosixmethod.feature.timer.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yopachara.fourtosixmethod.core.data.model.Step
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import java.math.RoundingMode

@Composable
fun StepsDisplay(
    timerDisplayState: TimerDisplayState,
    modifier: Modifier = Modifier
) {
    val currentStepIndex = timerDisplayState.getCurrentStateIndex()
    val isTimerActive = timerDisplayState.secondsRemaining != null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Brewing Timeline & Steps",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            timerDisplayState.recipe.steps.forEachIndexed { index, item ->
                val isActive = isTimerActive && index == currentStepIndex
                val isCompleted = isTimerActive && index < currentStepIndex

                val containerColor by animateColorAsState(
                    targetValue = when {
                        isActive -> MaterialTheme.colorScheme.primaryContainer
                        isCompleted -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    },
                    label = "containerColor"
                )

                val contentAlpha = when {
                    isActive -> 1.0f
                    isCompleted -> 0.45f
                    else -> 0.85f
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = containerColor),
                    elevation = if (isActive) CardDefaults.cardElevation(defaultElevation = 2.dp) else CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .alpha(contentAlpha),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Timeline Indicator Accent
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    color = when {
                                        isActive -> MaterialTheme.colorScheme.primary
                                        isCompleted -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                )
                        ) {
                            if (isCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Completed",
                                    tint = MaterialTheme.colorScheme.onSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Text(
                                    text = (index + 1).toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Step details
                        Column(modifier = Modifier.weight(1f)) {
                            val stepName = when (index) {
                                0 -> "1st Pour (Bloom & Sweetness)"
                                1 -> "2nd Pour (Acidity Balance)"
                                else -> "${index + 1}th Pour (Body Strength)"
                            }
                            Text(
                                text = stepName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.SemiBold,
                                color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Pour time: ${item.time}s",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Weights column
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "+${item.getWaterWithScale(1)} g",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "To: ${weightOnScale(index, timerDisplayState.recipe.steps)}g",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

        }

    }
}

fun weightOnScale(index: Int, steps: List<Step>): String {
    return if (index == 0) {
        steps[index].waterWeight.toBigDecimal().setScale(1, RoundingMode.UP).toString()
    } else {
        var total = 0f
        steps.forEachIndexed { currentIndex, step ->
            if (currentIndex <= index) {
                total += step.waterWeight
            }
        }
        total.toBigDecimal().setScale(1, RoundingMode.UP).toString()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStepsDisplay() {
    StepsDisplay(timerDisplayState = TimerDisplayState())
}