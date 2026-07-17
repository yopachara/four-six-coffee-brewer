package com.yopachara.fourtosixmethod.feature.timer.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import kotlin.math.roundToInt

const val MIN_HOT_RATIO = 10

@Composable
fun IcedDripDisplay(
    timerDisplayState: TimerDisplayState,
    onIcedDripToggle: (Boolean) -> Unit,
    onHotRatioChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val recipe = timerDisplayState.recipe

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Iced Drip Mode",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Brew concentrated over ice — hot water + ice equals the full yield",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = recipe.isIcedDrip,
                onCheckedChange = onIcedDripToggle
            )
        }

        if (recipe.isIcedDrip) {
            var sliderPosition by remember<MutableState<Int>> { mutableStateOf(recipe.hotRatio) }

            Text(
                text = "1:${recipe.getEffectiveHotRatio()}",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = sliderPosition.toFloat(),
                valueRange = MIN_HOT_RATIO.toFloat()..(recipe.ratio - 1).coerceAtLeast(MIN_HOT_RATIO)
                    .toFloat(),
                steps = (recipe.ratio - MIN_HOT_RATIO - 2).coerceAtLeast(0),
                onValueChange = {
                    sliderPosition = it.roundToInt()
                    onHotRatioChange(sliderPosition)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hot water",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${recipe.getHotWaterWeight().toInt()} g",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ice in server",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${recipe.getIceWeight().toInt()} g",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun PreviewIcedDripDisplay() {
    IcedDripDisplay(
        timerDisplayState = TimerDisplayState(
            recipe = Recipe(_isIcedDrip = true, _coffeeWeight = 20f, _ratio = 15, _hotRatio = 10)
        ),
        onIcedDripToggle = {},
        onHotRatioChange = {}
    )
}
