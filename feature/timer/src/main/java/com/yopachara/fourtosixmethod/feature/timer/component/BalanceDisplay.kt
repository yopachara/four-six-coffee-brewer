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
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState

@Composable
fun BalanceDisplay(
    timerDisplayState: TimerDisplayState,
    changeBalanceLevel: (Balance) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "Balance Profile",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Adjust flavor balance of sweetness vs acidity",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        SelectableOptionCardGroup(
            options = Balance.values().toList(),
            selected = timerDisplayState.recipe.balance,
            onSelect = changeBalanceLevel,
            label = { it.name },
            description = { balanceOption ->
                when (balanceOption) {
                    Balance.Basic -> "Perfect, classic balance"
                    Balance.Sweet -> "Brings out pleasant, round sweetness"
                    Balance.Acid -> "Highlights bright, crisp acidity"
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBalanceDisplay() {
    BalanceDisplay(timerDisplayState = TimerDisplayState(), changeBalanceLevel = {})
}