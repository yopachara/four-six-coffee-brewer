package com.yopachara.fourtosixmethod.feature.timer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeSettingBottomSheet(
    timerDisplayState: TimerDisplayState,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    onWeightChanged: (Float) -> Unit,
    onRatioChanged: (Int) -> Unit,
    onIcedDripToggle: (Boolean) -> Unit,
    onHotRatioChange: (Int) -> Unit,
    onBalanceChange: (Balance) -> Unit,
    onBodyChange: (Level) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        RecipeSettingBottomSheetContent(
            timerDisplayState = timerDisplayState,
            onDone = onDismissRequest,
            onWeightChanged = onWeightChanged,
            onRatioChanged = onRatioChanged,
            onIcedDripToggle = onIcedDripToggle,
            onHotRatioChange = onHotRatioChange,
            onBalanceChange = onBalanceChange,
            onBodyChange = onBodyChange
        )
    }
}

@Composable
fun RecipeSettingBottomSheetContent(
    timerDisplayState: TimerDisplayState,
    onDone: () -> Unit,
    onWeightChanged: (Float) -> Unit,
    onRatioChanged: (Int) -> Unit,
    onIcedDripToggle: (Boolean) -> Unit,
    onHotRatioChange: (Int) -> Unit,
    onBalanceChange: (Balance) -> Unit,
    onBodyChange: (Level) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RecipeSheetHeader(onDone = onDone)

        RecipeStatsRow(timerDisplayState = timerDisplayState)

        SettingCard {
            WeightDisplay(timerDisplayState, onWeightChanged)
        }

        SettingCard {
            RatioDisplay(timerDisplayState, onRatioChanged)
        }

        SettingCard {
            IcedDripDisplay(
                timerDisplayState = timerDisplayState,
                onIcedDripToggle = onIcedDripToggle,
                onHotRatioChange = onHotRatioChange
            )
        }

        SettingCard {
            BalanceDisplay(
                timerDisplayState = timerDisplayState,
                changeBalanceLevel = onBalanceChange
            )
        }

        SettingCard {
            BodyDisplay(
                timerDisplayState = timerDisplayState,
                changeBodyLevel = onBodyChange
            )
        }
    }
}

@Composable
private fun RecipeSheetHeader(
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Recipe",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Done",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onDone() }
                .padding(horizontal = 20.dp, vertical = 10.dp)
        )
    }
}

@Composable
private fun RecipeStatsRow(
    timerDisplayState: TimerDisplayState,
    modifier: Modifier = Modifier
) {
    val recipe = timerDisplayState.recipe

    val stats: List<Triple<String, String, Color>> = if (recipe.isIcedDrip) {
        listOf(
            Triple("Coffee", "${recipe.coffeeWeight}g", MaterialTheme.colorScheme.onSurface),
            Triple(
                "Hot water",
                "${recipe.getHotWaterWeight().toInt()}g",
                MaterialTheme.colorScheme.onSurface
            ),
            Triple("Ice", "${recipe.getIceWeight().toInt()}g", MaterialTheme.colorScheme.tertiary)
        )
    } else {
        listOf(
            Triple("Coffee", "${recipe.coffeeWeight}g", MaterialTheme.colorScheme.onSurface),
            Triple(
                "Water",
                "${recipe.getTotalWater().toInt()}g",
                MaterialTheme.colorScheme.onSurface
            ),
            Triple("Brew time", timerDisplayState.displayTotalSeconds, MaterialTheme.colorScheme.onSurface)
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            stats.forEachIndexed { index, (label, value, color) ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = value,
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                    Text(
                        text = label.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.8.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }

                if (index != stats.lastIndex) {
                    VerticalDivider(
                        modifier = Modifier.height(28.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeSettingBottomSheetContentPreview() {
    RecipeSettingBottomSheetContent(
        timerDisplayState = TimerDisplayState(
            recipe = Recipe(
                _isIcedDrip = true,
                _coffeeWeight = 30f,
                _ratio = 15,
                _hotRatio = 10,
            )
        ),
        onDone = {},
        onWeightChanged = {},
        onRatioChanged = {},
        onIcedDripToggle = {},
        onHotRatioChange = {},
        onBalanceChange = {},
        onBodyChange = {}
    )
}
