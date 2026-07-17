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
            .padding(vertical = 12.dp, horizontal = 4.dp)
    ) {
        Text(
            text = "Balance Profile",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        SegmentedOptionGroup(
            options = Balance.values().toList(),
            selected = timerDisplayState.recipe.balance,
            onSelect = changeBalanceLevel,
            label = { it.name }
        )

        Text(
            text = when (timerDisplayState.recipe.balance) {
                Balance.Basic -> "Perfect, classic balance"
                Balance.Sweet -> "Brings out pleasant, round sweetness"
                Balance.Acid -> "Highlights bright, crisp acidity"
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
fun PreviewBalanceDisplay() {
    BalanceDisplay(timerDisplayState = TimerDisplayState(), changeBalanceLevel = {})
}
