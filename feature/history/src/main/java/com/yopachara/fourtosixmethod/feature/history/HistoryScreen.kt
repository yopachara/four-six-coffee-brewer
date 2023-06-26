package com.yopachara.fourtosixmethod.feature.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.designsystem.theme.Typography
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
internal fun HistoryRoute(
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val historyState = viewModel.historyStateFlow.collectAsState()

    HistoryScreen(historyState)
}


@Composable
fun HistoryScreen(historyState: State<List<Recipe>>) {
    if (historyState.value.isNotEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            if (historyState.value.isNotEmpty()) {
                HistoryChart(historyState.value)
                LazyColumn(modifier = Modifier.padding(top = 24.dp, start = 8.dp, end = 8.dp)) {
                    items(historyState.value) { recipe ->
                        HistoryItem(recipe = recipe)
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Empty ☕",
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }
    }
}

@Composable
fun HistoryItem(recipe: Recipe) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),

        ) {

        Text(
            text = """${recipe.coffeeWeight} g""",
            style = Typography.bodySmall,
            modifier = Modifier.padding(
                horizontal = 12.dp
            )
        )
        Text(
            text = """1:${recipe.ratio}""",
            style = Typography.bodySmall,
            modifier = Modifier.padding(
                horizontal = 12.dp
            )
        )
        Text(
            text = """${recipe.balance}""",
            style = Typography.bodySmall,
            modifier = Modifier.padding(
                horizontal = 12.dp
            )
        )
        Text(
            text = """${recipe.level}""",
            style = Typography.bodySmall,
            modifier = Modifier.padding(
                horizontal = 12.dp
            )
        )

        val firstApiFormat = SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())
        firstApiFormat.format(recipe.createAt)
        Text(

            text = """${firstApiFormat.format(recipe.createAt)}""",
            style = Typography.bodySmall,
            modifier = Modifier.padding(
                horizontal = 12.dp
            )
        )
    }
}