package com.yopachara.fourtosixmethod.feature.timer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import com.yopachara.fourtosixmethod.util.roundTo

@Composable
fun WeightDisplay(
    timerDisplayState: TimerDisplayState,
    setWeight: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 4.dp)
    ) {
        Text(
            text = "Coffee Weight",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        var sliderPosition by remember { mutableStateOf(timerDisplayState.recipe.coffeeWeight * 10) }

        fun updateWeight(value: Float) {
            sliderPosition = value * 10
            setWeight(value)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilledTonalIconButton(
                onClick = {
                    if (timerDisplayState.recipe.coffeeWeight > 10) {
                        updateWeight(timerDisplayState.recipe.coffeeWeight.minus(0.1f).roundTo(1))
                    }
                },
                enabled = timerDisplayState.recipe.coffeeWeight > 10
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Decrease coffee weight"
                )
            }

            Text(
                text = "${timerDisplayState.recipe.coffeeWeight}g",
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            FilledTonalIconButton(
                onClick = {
                    if (timerDisplayState.recipe.coffeeWeight < 55) {
                        updateWeight(timerDisplayState.recipe.coffeeWeight.plus(0.1f).roundTo(1))
                    }
                },
                enabled = timerDisplayState.recipe.coffeeWeight < 55
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase coffee weight"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = sliderPosition,
            valueRange = 100f..550f,
            steps = 450,
            onValueChange = {
                sliderPosition = it
                setWeight(sliderPosition.div(10f).roundTo(1))
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWeightDisplay() {
    WeightDisplay(timerDisplayState = TimerDisplayState(), setWeight = {})
}
