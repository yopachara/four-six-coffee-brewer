package com.yopachara.fourtosixmethod.feature.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import com.yopachara.fourtosixmethod.util.roundTo

@Composable
fun WeightDisplay(timerDisplayState: TimerDisplayState, setWeight: (Float) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        var sliderPosition by remember { mutableStateOf(timerDisplayState.recipe.coffeeWeight * 10) }
        fun updateWeight(value: Float) {
            sliderPosition = value * 10
            setWeight(value)
        }

        Row {
            Text(
                text = "${timerDisplayState.recipe.coffeeWeight} g",
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
            )

            Button(
                enabled = timerDisplayState.recipe.coffeeWeight < 40,
                onClick = {
                    updateWeight(timerDisplayState.recipe.coffeeWeight.plus(0.1f).roundTo(1))
                }, modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = "+0.1", color = Color.White, fontStyle = FontStyle.Italic)
            }
            Button(
                enabled = timerDisplayState.recipe.coffeeWeight > 10,
                onClick = {
                    updateWeight(
                        timerDisplayState.recipe.coffeeWeight.minus(0.1f).roundTo(1)
                    )
                }) {
                Text(text = "-0.1", color = Color.White, fontStyle = FontStyle.Italic)
            }
        }


        Slider(
            value = sliderPosition,
            valueRange = 100f..400f,
            steps = 300,
            onValueChange = {
                sliderPosition = it
                setWeight(
                    sliderPosition.div(10f).roundTo(1)
                )
            },
        )


    }

}

@Preview(showBackground = true)
@Composable
fun PreviewWeightDisplay() {
    WeightDisplay(timerDisplayState = TimerDisplayState()) {}
}