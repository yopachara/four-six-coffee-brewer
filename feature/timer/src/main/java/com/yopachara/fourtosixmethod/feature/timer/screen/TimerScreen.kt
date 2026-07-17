package com.yopachara.fourtosixmethod.feature.timer.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yopachara.fourtosixmethod.core.data.model.Balance
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.feature.timer.component.BalanceDisplay
import com.yopachara.fourtosixmethod.feature.timer.component.BodyDisplay
import com.yopachara.fourtosixmethod.feature.timer.component.RatioDisplay
import com.yopachara.fourtosixmethod.feature.timer.component.StepsDisplay
import com.yopachara.fourtosixmethod.feature.timer.component.TimerDisplay
import com.yopachara.fourtosixmethod.feature.timer.component.WeightDisplay
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState
import com.yopachara.fourtosixmethod.feature.timer.viewmodel.TimerViewModel

@Composable
internal fun TimerRoute(
    modifier: Modifier = Modifier,
    viewModel: TimerViewModel = hiltViewModel(),
) {
    val timerState by viewModel.timerDisplayStateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* no-op: timer still runs, notification just won't show without this */ }

    TimerScreen(
        timerDisplayState = timerState,
        toggleStartPause = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            viewModel.toggleTime()
        },
        onStop = viewModel::stopTime,
        onWeightChanged = viewModel::setCoffeeWeight,
        onRatioChanged = viewModel::setCoffeeRatio,
        onBalanceChange = viewModel::setCoffeeBalance,
        onBodyChange = viewModel::setCoffeeLevel,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimerScreen(
    timerDisplayState: TimerDisplayState,
    toggleStartPause: () -> Unit,
    onStop: () -> Unit,
    onWeightChanged: (Float) -> Unit,
    onRatioChanged: (Int) -> Unit,
    onBalanceChange: (Balance) -> Unit,
    onBodyChange: (Level) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                shape = RoundedCornerShape(16.dp),
                text = { Text("Recipe Settings") },
                icon = { Icon(Icons.Filled.Settings, contentDescription = "Recipe Settings") },
                onClick = {
                    showBottomSheet = true
                },
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
        ) {
            TimerDisplay(
                timerDisplayState = timerDisplayState,
                toggleStartPause = toggleStartPause,
                onStop = onStop
            )

            StepsDisplay(timerDisplayState = timerDisplayState)

            Spacer(modifier = Modifier.height(100.dp))
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(start = 12.dp, end = 12.dp, bottom = 48.dp)
                ) {
                    WeightDisplay(timerDisplayState, onWeightChanged)
                    Spacer(modifier = Modifier.height(12.dp))
                    RatioDisplay(timerDisplayState, onRatioChanged)
                    Spacer(modifier = Modifier.height(12.dp))
                    BalanceDisplay(
                        timerDisplayState = timerDisplayState,
                        changeBalanceLevel = onBalanceChange
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    BodyDisplay(
                        timerDisplayState = timerDisplayState,
                        changeBodyLevel = onBodyChange
                    )
                }
            }
        }
    }
}

