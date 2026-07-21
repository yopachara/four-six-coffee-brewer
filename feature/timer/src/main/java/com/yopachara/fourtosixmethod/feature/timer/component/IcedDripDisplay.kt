package com.yopachara.fourtosixmethod.feature.timer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ICED DRIP",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Hot water + ice equal the full yield",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Switch(
                checked = recipe.isIcedDrip,
                onCheckedChange = onIcedDripToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.surface,
                    checkedTrackColor = MaterialTheme.colorScheme.tertiary,
                    checkedBorderColor = MaterialTheme.colorScheme.tertiary
                )
            )
        }

        if (recipe.isIcedDrip) {
            var sliderPosition by remember<MutableState<Int>> { mutableStateOf(recipe.hotRatio) }
            val hotWaterWeight = recipe.getHotWaterWeight()
            val iceWeight = recipe.getIceWeight()
            val hotWaterFraction = (hotWaterWeight / (hotWaterWeight + iceWeight).coerceAtLeast(0.01f))
                .coerceIn(0f, 1f)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50)),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(hotWaterFraction.coerceAtLeast(0.001f))
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.primary)
                )
                Box(
                    modifier = Modifier
                        .weight((1f - hotWaterFraction).coerceAtLeast(0.001f))
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.tertiary)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            IcedDripStatRow(
                dotColor = MaterialTheme.colorScheme.primary,
                label = "Hot water · pour 1:${recipe.getEffectiveHotRatio()}",
                value = "${hotWaterWeight.toInt()}g",
                valueColor = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            IcedDripStatRow(
                dotColor = MaterialTheme.colorScheme.tertiary,
                label = "Ice in server",
                value = "${iceWeight.toInt()}g",
                valueColor = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "HOT POUR RATIO",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Slider(
                value = sliderPosition.toFloat(),
                valueRange = MIN_HOT_RATIO.toFloat()..(recipe.ratio - 1).coerceAtLeast(MIN_HOT_RATIO)
                    .toFloat(),
                steps = (recipe.ratio - MIN_HOT_RATIO - 2).coerceAtLeast(0),
                onValueChange = {
                    sliderPosition = it.roundToInt()
                    onHotRatioChange(sliderPosition)
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.tertiary,
                    activeTrackColor = MaterialTheme.colorScheme.tertiary,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun IcedDripStatRow(
    dotColor: Color,
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        Text(
            text = value,
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
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
