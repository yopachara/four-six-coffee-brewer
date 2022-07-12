package com.yopachara.fourtosixmethod.ui.home

import android.widget.Toast
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.yopachara.fourtosixmethod.data.TimerState
import com.yopachara.fourtosixmethod.util.roundTo
import kotlinx.coroutines.delay
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

@Composable
fun WeightDisplay(timerState: TimerState, setWeight: (Float) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        var sliderPosition by remember { mutableStateOf(timerState.recipe.coffeeWeight * 10) }
        fun updateWeight(value: Float) {
            sliderPosition = value * 10
            setWeight(value)
        }

        Row {
            Text(
                text = "${timerState.recipe.coffeeWeight} g",
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
            )

            Button(
                enabled = timerState.recipe.coffeeWeight < 40,
                onClick = {
                    updateWeight(timerState.recipe.coffeeWeight.plus(0.1f).roundTo(1))
                }, modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                Text(text = "+0.1", fontStyle = FontStyle.Italic)
            }
            Button(
                enabled = timerState.recipe.coffeeWeight > 10,
                onClick = {
                    updateWeight(
                        timerState.recipe.coffeeWeight.minus(0.1f).roundTo(1)
                    )
                }) {
                Text(text = "-0.1", fontStyle = FontStyle.Italic)
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
    WeightDisplay(timerState = TimerState()) {}
}