package com.yopachara.fourtosixmethod.feature.timer

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState

@Composable
fun TimerDisplay(
    timerDisplayState: TimerDisplayState,
    toggleStartPause: () -> Unit,
    onStop: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        val context = LocalContext.current

        val stateAnimateProcess = animateFloatAsState(
            targetValue = timerDisplayState.statePercentage,
            animationSpec = tween(durationMillis = 100, easing = LinearEasing)
        ).value

        val totalAnimateProcess = animateFloatAsState(
            targetValue = timerDisplayState.progressPercentage,
            animationSpec = tween(durationMillis = 100, easing = LinearEasing)
        )

        Text(
            text = timerDisplayState.displaySeconds,
            fontSize = 62.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth(1f)
                .clickable { toggleStartPause() }
        )

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = Color.Black)
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = 24.dp)
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = "weight",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier

                )
                Text(
                    text = "${timerDisplayState.getWaterWeightCurrentState()} g",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()

                )
            }
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .align(Alignment.Center)
                    .height(50.dp)
                    .background(color = Color.Black)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = 24.dp)
                    .align(Alignment.CenterEnd)
            ) {
                Text(
                    text = "ratio",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Start,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()

                )
                Text(
                    text = "1:${timerDisplayState.recipe.ratio}",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()

                )
            }
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = Color.Black)
        )
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = 24.dp)
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = "balance",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier

                )
                Text(
                    text = "${timerDisplayState.recipe.balance}",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()

                )
            }
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(50.dp)
                    .align(Alignment.Center)
                    .background(color = Color.Black)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(horizontal = 24.dp)
                    .align(Alignment.CenterEnd)
            ) {
                Text(
                    text = "body",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier

                )

                Text(
                    text = "${timerDisplayState.recipe.level}",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()

                )
            }
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = Color.Black)
        )

        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = "total time ${timerDisplayState.totalSeconds} second",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            LinearProgressIndicator(
                progress = 1 - totalAnimateProcess.value,
                modifier = Modifier
                    .height(6.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.requiredHeight(12.dp))

            Text(
                text = "state time ${timerDisplayState.getCurrentStateMaxTime()} second",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            LinearProgressIndicator(
                progress = stateAnimateProcess,
                Modifier
                    .height(6.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                strokeCap = StrokeCap.Round

            )

        }

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = Color.Black)
        )

        Row(modifier = Modifier.padding(vertical = 12.dp)) {
            FilledIconButton(
                onClick = {
                    toggleStartPause()
                },
                modifier = Modifier.size(48.dp),
            ) {
                if (timerDisplayState.isPlaying()) {
                    Icon(
                        Icons.Filled.Pause, contentDescription = "pause",
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                } else {
                    Icon(
                        Icons.Filled.PlayArrow, contentDescription = "play",
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            FilledIconButton(
                onClick = { onStop() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Filled.Stop,
                    contentDescription = "stop",
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = Color.Black)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimerDisplay() {
    TimerDisplay(timerDisplayState = TimerDisplayState(60), {}) {}
}