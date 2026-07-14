package com.yopachara.fourtosixmethod.feature.timer.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import kotlin.math.roundToInt

@Composable
fun RatioDisplay(
    timerDisplayState: TimerDisplayState,
    setRatioChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        var sliderPosition by remember<MutableState<Int>> { mutableStateOf(timerDisplayState.recipe.ratio) }

        Text(
            text = "Brew Ratio",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Adjust the ratio of coffee to water (1:X)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = "1:${timerDisplayState.recipe.ratio}",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = sliderPosition.toFloat(),
            valueRange = 10f..16f,
            steps = 5,
            onValueChange = {
                sliderPosition = it.roundToInt()
                setRatioChange(sliderPosition)
            },
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRatioDisplay() {
    RatioDisplay(timerDisplayState = TimerDisplayState(), setRatioChange = {})
}