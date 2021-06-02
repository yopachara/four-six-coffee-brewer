package com.yopachara.fourtosixmethod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yopachara.fourtosixmethod.data.TimerState
import com.yopachara.fourtosixmethod.ui.home.*
import com.yopachara.fourtosixmethod.ui.theme.FourSixMethodTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FourSixMethodTheme {
                val vm: TimerViewModel = viewModel()
                val timerState = vm.timerStateFlow.collectAsState()
                Column() {
                    TimerDisplay(timerState.value, vm::toggleStart)
                    WeightDisplay(timerState.value) { weight ->
                        vm.setWeight(weight)
                    }
                    RatioDisplay(timerState.value) { ratio ->
                        vm.setRatio(ratio)
                    }
                    Row {
                        BalanceDisplay(timerState = timerState.value) {
                            vm.setBalance(it)
                        }
                        BodyDisplay(timerState = timerState.value) {
                            vm.setLevel(it)
                        }
                    }

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
            TimerDisplay(timerState) {}
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