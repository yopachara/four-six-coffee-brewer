package com.yopachara.fourtosixmethod.feature.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yopachara.fourtosixmethod.core.data.model.TimerState
import kotlin.math.roundToInt

@Composable
fun RatioDisplay(timerState: TimerState, setRatioChange: (Int) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        var sliderPosition by remember<MutableState<Int>> { mutableStateOf(timerState.recipe.ratio) }

        Text(
            text = "1:${timerState.recipe.ratio.toString()}",
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
    RatioDisplay(timerState = TimerState()) {}
}