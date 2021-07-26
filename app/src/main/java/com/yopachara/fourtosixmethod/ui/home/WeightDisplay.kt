package com.yopachara.fourtosixmethod.ui.home

import androidx.compose.foundation.layout.*
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
import com.yopachara.fourtosixmethod.data.TimerState
import java.math.RoundingMode

@Composable
fun WeightDisplay(timerState: TimerState, setWeight: (Float) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        var sliderPosition by remember { mutableStateOf(timerState.recipe.coffeeWeight * 10) }

        Text(
            text = "${timerState.recipe.coffeeWeight.toString()} g",
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
        )
        Slider(
            value = sliderPosition,
            valueRange = 100f..400f,
            steps = 300,
            onValueChange = {
                sliderPosition = it
                setWeight(
                    sliderPosition.div(10f).toBigDecimal().setScale(1, RoundingMode.UP).toFloat()
                )
            },
        )

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWeightDisplay() {
    WeightDisplay(timerState = TimerState()){}
}