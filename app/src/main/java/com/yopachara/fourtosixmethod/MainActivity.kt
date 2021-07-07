package com.yopachara.fourtosixmethod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yopachara.fourtosixmethod.data.TimerState
import com.yopachara.fourtosixmethod.ui.home.*
import com.yopachara.fourtosixmethod.ui.theme.FourSixMethodTheme
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FourSixMethodTheme {
                val vm: TimerViewModel = viewModel()
                val timerState = vm.timerStateFlow.collectAsState()
                val scope = rememberCoroutineScope()

                val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                    bottomSheetState = rememberBottomSheetState(
                        initialValue = BottomSheetValue.Collapsed
                    )
                )

                Column() {
                    TimerDisplay(timerState.value) {
                        vm.toggleStart()
                        scope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
                    BottomSheetScaffold(
                        scaffoldState = bottomSheetScaffoldState,
                        sheetContent = {
                            Column() {

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
                        },
                        sheetPeekHeight = 48.dp,
                        floatingActionButtonPosition = FabPosition.End,
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = {
                                    scope.launch {
                                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        } else {
                                            bottomSheetScaffoldState.bottomSheetState.collapse()
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Settings,
                                    contentDescription = "Localized description")
                            }
                        },
                    ) { innerPadding ->
                        Text(text = "Hello !")


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