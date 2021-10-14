package com.yopachara.fourtosixmethod.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.yopachara.fourtosixmethod.data.Step
import com.yopachara.fourtosixmethod.data.TimerState
import com.yopachara.fourtosixmethod.ui.theme.Typography
import java.math.RoundingMode
import java.sql.Time
import kotlin.math.roundToInt

@Composable
fun StepsDisplay(timerState: TimerState) {

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
            Text(text = "step",
                style = Typography.body1,
                modifier = Modifier.padding(
                    horizontal = 12.dp
                )
            )
            Text(text = "time",
                style = Typography.body1,
                modifier = Modifier.padding(
                    horizontal = 12.dp
                )
            )
            Text(text = "water",
                style = Typography.body1,
                modifier = Modifier.padding(
                    horizontal = 12.dp
                )
            )
            Text(text = "total",
                style = Typography.body1,
                modifier = Modifier.padding(
                    horizontal = 12.dp
                )
            )
        }

        LazyColumn {
            itemsIndexed(timerState.recipe.steps) { index, item ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(vertical = 2.dp)
                        .background(
                            color = if (index == timerState.getCurrentStateIndex()) {
                                MaterialTheme.colors.secondaryVariant
                            } else {
                                Color.Transparent
                            }
                        ),

                    ) {
                    Text(text = """${(index+1)}""",
                        style = Typography.body1,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
                    )
                    Text(text = """${item.time} sec""",
                        style = Typography.body1,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
                    )
                    Text(text = """${item.getWaterWithScale(1)} g""",
                        style = Typography.body1,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
                    )
                    Text(text = """${weightOnScale(index, timerState.recipe.steps)} g""",
                        style = Typography.body1,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
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
    StepsDisplay(timerState = TimerState())
}