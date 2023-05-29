package com.yopachara.fourtosixmethod

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yopachara.fourtosixmethod.core.database.model.TimerState
import com.yopachara.fourtosixmethod.core.designsystem.theme.FourSixMethodTheme
import com.yopachara.fourtosixmethod.ui.home.BalanceDisplay
import com.yopachara.fourtosixmethod.ui.home.BodyDisplay
import com.yopachara.fourtosixmethod.ui.home.RatioDisplay
import com.yopachara.fourtosixmethod.ui.home.StepsDisplay
import com.yopachara.fourtosixmethod.ui.home.TimerDisplay
import com.yopachara.fourtosixmethod.ui.home.TimerViewModel
import com.yopachara.fourtosixmethod.ui.home.WeightDisplay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FourSixMethodTheme {
                val vm: TimerViewModel = viewModel()
                val historyState = vm.historyStateFlow.collectAsState()
                val timerState = vm.timerStateFlow.collectAsState()
                val scope = rememberCoroutineScope()

                val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                    bottomSheetState = rememberStandardBottomSheetState(
                    )
                )

                Log.i("timerState",timerState.value.toString())

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
                            vm.toggleStart()
//                            scope.launch {
//                                bottomSheetScaffoldState.bottomSheetState.hide()
//                            }
                        }

                        StepsDisplay(timerState.value)

//                    }

                }

            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FourSixMethodTheme {
        val timerState = TimerState()
        Column() {
            TimerDisplay(timerState, emptyList()) {}
            WeightDisplay(timerState) { weight ->
            }
            RatioDisplay(timerState) { ratio ->
            }
            Row {
                BalanceDisplay(timerState = timerState) {
                }
                BodyDisplay(timerState = timerState) {
                }
            }

        }

    }
}