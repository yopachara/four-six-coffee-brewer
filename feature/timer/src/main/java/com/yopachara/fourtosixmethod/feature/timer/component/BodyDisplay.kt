package com.yopachara.fourtosixmethod.feature.timer.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "Body Strength",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Set the extraction strength & mouthfeel",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        SelectableOptionCardGroup(
            options = Level.values().toList(),
            selected = timerDisplayState.recipe.level,
            onSelect = changeBodyLevel,
            label = { it.displayName },
            description = { levelOption ->
                when (levelOption) {
                    Level.Basic -> "Standard strength, clean & defined"
                    Level.Week -> "Lighter strength, smooth & gentle"
                    Level.Strong -> "Higher strength, rich, bold & intense"
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBodyDisplay() {
    BodyDisplay(timerDisplayState = TimerDisplayState(), changeBodyLevel = {})
}