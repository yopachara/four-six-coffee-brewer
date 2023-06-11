import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.yopachara.fourtosixmethod.feature.timer.StepsDisplay
import com.yopachara.fourtosixmethod.feature.timer.TimerDisplay
import com.yopachara.fourtosixmethod.feature.timer.TimerViewModel

@Composable
internal fun TimerRoute(
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TimerViewModel = hiltViewModel(),
) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TimerScreen(viewModel)
}

@Composable
internal fun TimerScreen(
    vm: TimerViewModel
) {


    val historyState = vm.historyStateFlow.collectAsState()
    val timerState = vm.timerStateFlow.collectAsState()

    Log.i("timerState", timerState.value.toString())

    Column() {
//                    BottomSheetScaffold(
//                        scaffoldState = bottomSheetScaffoldState,
//                        sheetContent = {
//                            Column(
//                                modifier = Modifier
//                                    .padding(top = 40.dp)
//                                    .background(color = Color.White)
//                            ) {
//
//                                WeightDisplay(timerState.value) { weight ->
//                                    vm.setWeight(weight)
//                                }
//                                RatioDisplay(timerState.value) { ratio ->
//                                    vm.setRatio(ratio)
//                                }
//                                Row {
//                                    BalanceDisplay(timerState = timerState.value) {
//                                        vm.setBalance(it)
//                                    }
//                                    BodyDisplay(timerState = timerState.value) {
//                                        vm.setLevel(it)
//                                    }
//                                }
//
//                            }
//                        },
//                        sheetShadowElevation = 0.dp,
//                        sheetTonalElevation = 0.dp,
//                        sheetPeekHeight = 0.dp,
//
//                    ) {
        TimerDisplay(timerState.value, historyState.value) {
            vm.toggleTime()
//                            scope.launch {
//                                bottomSheetScaffoldState.bottomSheetState.hide()
//                            }
        }

        StepsDisplay(timerState.value)

//                    }


    }
}
