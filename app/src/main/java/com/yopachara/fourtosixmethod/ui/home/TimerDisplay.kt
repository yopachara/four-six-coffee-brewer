package com.yopachara.fourtosixmethod.ui.home

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.yopachara.fourtosixmethod.data.TimerState

@Composable
fun TimerDisplay(timerState: TimerState, toggleStartStop: () -> Unit) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        val (outerCircular, innerCircular, totalTimer, stateTimer, stateWeight) = createRefs()
        val context = LocalContext.current

        CircularProgressIndicator(
            progress = timerState.progressPercentage,
            strokeWidth = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(24.dp)
                .constrainAs(ref = outerCircular) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )

        CircularProgressIndicator(
            timerState.statePercentage,
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(36.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            Toast
                                .makeText(context, "LongPress", Toast.LENGTH_SHORT)
                                .show()
                        },
                        onTap = {
                            toggleStartStop()
                        }
                    )
                }

                .constrainAs(ref = innerCircular) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        )
        Text(
            text = timerState.displaySeconds,
            fontSize = 62.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(totalTimer) {
                top.linkTo(outerCircular.top)
                bottom.linkTo(outerCircular.bottom)
                start.linkTo(outerCircular.start)
                end.linkTo(outerCircular.end)
                width = Dimension.wrapContent
                height = Dimension.wrapContent
            }
        )

        Text(
            text = timerState.getCurrentStateTime().toString(),
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(stateTimer) {
                top.linkTo(totalTimer.bottom, 12.dp)
                start.linkTo(totalTimer.start)
                end.linkTo(totalTimer.end)
                width = Dimension.wrapContent
                height = Dimension.wrapContent
            }
        )

        Text(
            text = "${timerState.getWaterWeightCurrentState()} g",
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(stateWeight) {
                bottom.linkTo(totalTimer.top, 12.dp)
                start.linkTo(totalTimer.start)
                end.linkTo(totalTimer.end)
                width = Dimension.wrapContent
                height = Dimension.wrapContent
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimerDisplay() {
    TimerDisplay(timerState = TimerState(60)) {}
}