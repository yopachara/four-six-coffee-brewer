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
import kotlin.math.roundToInt

@Composable
fun RatioDisplay(timerState: TimerState, setRatioChange: (Int) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        var sliderPosition by remember { mutableStateOf(timerState.ratio) }

        Text(
            text = "1:${timerState.ratio.toString()}",
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
        )
        Slider(
            value = sliderPosition.toFloat(),
            valueRange = 10f..16f,
            steps = 5,
            onValueChange = {
                sliderPosition = it.roundToInt()
                setRatioChange(sliderPosition)
            },
        )

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRatioDisplay() {
    RatioDisplay(timerState = TimerState()){}
}