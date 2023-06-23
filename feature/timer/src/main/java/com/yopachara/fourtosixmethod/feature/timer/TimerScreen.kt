import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.feature.timer.BalanceDisplay
import com.yopachara.fourtosixmethod.feature.timer.BodyDisplay
import com.yopachara.fourtosixmethod.feature.timer.RatioDisplay
import com.yopachara.fourtosixmethod.feature.timer.StepsDisplay
import com.yopachara.fourtosixmethod.feature.timer.TimerDisplay
import com.yopachara.fourtosixmethod.feature.timer.TimerViewModel
import com.yopachara.fourtosixmethod.feature.timer.WeightDisplay
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import kotlinx.coroutines.launch

@Composable
internal fun TimerRoute(
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TimerViewModel = hiltViewModel(),
) {
    val timerState = viewModel.timerDisplayStateFlow.collectAsState()

    TimerScreen(
        timerDisplayState = timerState,
        toggleStartPause = viewModel::toggleTime,
        onStop = viewModel::stopTime,
        onWeightChanged = viewModel::setWeight,
        onRatioChanged = viewModel::setRatio,
        onBalanceChange = viewModel::setBalance,
        onBodyChange = viewModel::setLevel,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimerScreen(
    timerDisplayState: State<TimerDisplayState>,
    toggleStartPause: () -> Unit,
    onStop: () -> Unit,
    onWeightChanged: (Float) -> Unit,
    onRatioChanged: (Int) -> Unit,
    onBalanceChange: (Balance) -> Unit,
    onBodyChange: (Level) -> Unit,
    modifier: Modifier,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    shape = RoundedCornerShape(10.dp),
                    text = { Text("Settings") },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "") },
                    onClick = {
                        showBottomSheet = true
                    },
                )
            }
        ) { contentPadding ->
            // Screen content

            Column(modifier = Modifier.padding(contentPadding)) {
                TimerDisplay(timerDisplayState.value, {
                    toggleStartPause()
                }) {
                    onStop()
                }
                StepsDisplay(timerDisplayState.value)
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    // Sheet content
                    Column(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 12.dp, bottom = 48.dp)
                    ) {
                        WeightDisplay(timerDisplayState.value, onWeightChanged)
                        RatioDisplay(timerDisplayState.value, onRatioChanged)
                        Row {
                            BalanceDisplay(
                                timerDisplayState = timerDisplayState.value,
                                changeBalanceLevel = onBalanceChange
                            )
                            BodyDisplay(
                                timerDisplayState = timerDisplayState.value,
                                changeBodyLevel = onBodyChange
                            )
                        }
                    }
                }
            }
        }
    }
}

