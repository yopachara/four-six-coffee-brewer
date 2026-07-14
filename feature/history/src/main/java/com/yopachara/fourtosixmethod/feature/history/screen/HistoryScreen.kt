package com.yopachara.fourtosixmethod.feature.history.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.feature.history.component.HistoryContent
import com.yopachara.fourtosixmethod.feature.history.component.HistoryEmptyState
import com.yopachara.fourtosixmethod.feature.history.component.previewRecipes
import com.yopachara.fourtosixmethod.feature.history.viewmodel.HistoryViewModel

@Composable
internal fun HistoryRoute(
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val historyState by viewModel.historyStateFlow.collectAsStateWithLifecycle()
    Scaffold(
        // Bottom inset is already reserved by the outer app Scaffold's bottomBar;
        // only the top status bar still needs handling here.
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top),
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                HistoryScreen(
                    historyState = historyState,
                    modifier = modifier
                )
            }
        }
    )
}

@Composable
fun HistoryScreen(
    historyState: List<Recipe>,
    modifier: Modifier = Modifier
) {
    if (historyState.isNotEmpty()) {
        HistoryContent(historyState = historyState, modifier = modifier)
    } else {
        HistoryEmptyState(modifier = modifier)
    }
}

@Preview(showBackground = true, heightDp = 700)
@Composable
private fun PreviewHistoryScreen() {
    HistoryScreen(historyState = previewRecipes())
}

@Preview(showBackground = true)
@Composable
private fun PreviewHistoryScreenEmpty() {
    HistoryScreen(historyState = emptyList())
}