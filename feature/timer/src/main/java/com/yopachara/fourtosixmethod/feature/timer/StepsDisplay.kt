package com.yopachara.fourtosixmethod.feature.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yopachara.fourtosixmethod.core.data.model.Step
import com.yopachara.fourtosixmethod.core.designsystem.theme.Typography
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import java.math.RoundingMode

@Composable
fun StepsDisplay(timerDisplayState: TimerDisplayState) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()

        ) {
            Text(
                text = "step",
                style = Typography.bodySmall,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            )
            Text(
                text = "time",
                style = Typography.bodySmall,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            )
            Text(
                text = "water",
                style = Typography.bodySmall,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            )
            Text(
                text = "total",
                style = Typography.bodySmall,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            )
        }

        LazyColumn {
            itemsIndexed(timerDisplayState.recipe.steps) { index, item ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(vertical = 2.dp)
                        .background(
                            color = if (index == timerDisplayState.getCurrentStateIndex()) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                Color.Transparent
                            }
                        ),

                    ) {
                    Text(
                        text = """${(index + 1)}""",
                        style = Typography.bodySmall,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        color = if (index == timerDisplayState.getCurrentStateIndex()) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        }
                    )
                    Text(
                        text = """${item.time} sec""",
                        style = Typography.bodySmall,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        color = if (index == timerDisplayState.getCurrentStateIndex()) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        }
                    )
                    Text(
                        text = """${item.getWaterWithScale(1)} g""",
                        style = Typography.bodySmall,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        color = if (index == timerDisplayState.getCurrentStateIndex()) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        }
                    )
                    Text(
                        text = """${weightOnScale(index, timerDisplayState.recipe.steps)} g""",
                        style = Typography.bodySmall,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        color = if (index == timerDisplayState.getCurrentStateIndex()) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        }
                    )
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