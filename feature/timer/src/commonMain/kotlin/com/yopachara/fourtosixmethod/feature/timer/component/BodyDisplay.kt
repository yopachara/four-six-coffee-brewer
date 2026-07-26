package com.yopachara.fourtosixmethod.feature.timer.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.model.displayName
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState

@Composable
fun BodyDisplay(
    timerDisplayState: TimerDisplayState,
    changeBodyLevel: (Level) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Body Strength",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        SegmentedOptionGroup(
            options = Level.values().toList(),
            selected = timerDisplayState.recipe.level,
            onSelect = changeBodyLevel,
            label = { it.displayName }
        )

        Text(
            text = when (timerDisplayState.recipe.level) {
                Level.Basic -> "Standard strength, clean & defined"
                Level.Week -> "Lighter strength, smooth & gentle"
                Level.Strong -> "Higher strength, rich, bold & intense"
            },
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBodyDisplay() {
    BodyDisplay(timerDisplayState = TimerDisplayState(), changeBodyLevel = {})
}
