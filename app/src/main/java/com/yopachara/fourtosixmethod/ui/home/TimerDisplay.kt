package com.yopachara.fourtosixmethod.ui.home

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.yopachara.fourtosixmethod.data.Recipe
import com.yopachara.fourtosixmethod.data.TimerState
import com.yopachara.fourtosixmethod.ui.theme.Typography
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun TimerDisplay(timerState: TimerState, historyState: List<Recipe>, toggleStartStop: () -> Unit) {
    var rotated by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(500)
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!rotated) 1f else 0.1f,
        animationSpec = tween(500)
    )

    val animateBack by animateFloatAsState(
        targetValue = if (rotated) 0.1f else 1f,
        animationSpec = tween(500)
    )

    val rotationHistory by animateFloatAsState(
        targetValue = if (rotated) 180f else -180f,
        animationSpec = tween(500)
    )

    val animateHistoryFront by animateFloatAsState(
        targetValue = if (!rotated) 0.1f else 1f,
        animationSpec = tween(500)
    )

    val animateHistoryBack by animateFloatAsState(
        targetValue = if (rotated) 1f else 0.1f,
        animationSpec = tween(500)
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
            .clickable {
                rotated = !rotated
            }
    ) {
        val (outerCircular, innerCircular, totalTimer, stateTimer, stateWeight) = createRefs()
        val context = LocalContext.current

        val stateAnimateProcess = animateFloatAsState(
            targetValue = timerState.statePercentage,

            animationSpec = TweenSpec(1000)
        ).value

        val totalAnimateProcess = animateFloatAsState(
            targetValue = timerState.progressPercentage,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        ).value

        Text(text = if (rotated) "Tap to back" else "Click to see history",
            modifier = Modifier
                .padding(4.dp)
                .graphicsLayer {
                    rotationY = rotation
                })

        LazyColumn(modifier = Modifier.padding(top = 24.dp, start = 8.dp, end = 8.dp)) {
            itemsIndexed(historyState) { index, recipe ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(vertical = 2.dp)
                        .zIndex(if (rotated) 1f else -1f)
                        .graphicsLayer {
                            alpha = if (rotated) animateHistoryBack else animateHistoryFront
                            rotationY = rotationHistory
                        },

                    ) {

                    Text(
                        text = """${recipe.coffeeWeight} g""",
                        style = Typography.body1,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
                    )
                    Text(
                        text = """1:${recipe.ratio}""",
                        style = Typography.body1,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
                    )
                    Text(
                        text = """${recipe.balance}""",
                        style = Typography.body1,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
                    )
                    Text(
                        text = """${recipe.level}""",
                        style = Typography.body1,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
                    )

                    val firstApiFormat = SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())
                    firstApiFormat.format(recipe.createAt)
                    Text(

                        text = """${firstApiFormat.format(recipe.createAt)}""",
                        style = Typography.body1,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
                    )
                }
            }

        }

        CircularProgressIndicator(
            progress = totalAnimateProcess,
            strokeWidth = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(24.dp)
                .zIndex(if (!rotated) 1f else -1f)
                .constrainAs(ref = outerCircular) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .graphicsLayer {
                    alpha = if (rotated) animateBack else animateFront
                }
        )

        CircularProgressIndicator(
            stateAnimateProcess,
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(36.dp)
                .zIndex(if (!rotated) 1f else -1f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            Toast
                                .makeText(context, "LongPress", Toast.LENGTH_SHORT)
                                .show()
                        },
                        onTap = {
                            if (!rotated) {
                                toggleStartStop()
                            }
                        }
                    )

                }

                .constrainAs(ref = innerCircular) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .graphicsLayer {
                    alpha = if (rotated) animateBack else animateFront
                }
        )
        Text(
            text = timerState.displaySeconds,
            fontSize = 62.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(totalTimer) {
                    top.linkTo(outerCircular.top)
                    bottom.linkTo(outerCircular.bottom)
                    start.linkTo(outerCircular.start)
                    end.linkTo(outerCircular.end)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
                .graphicsLayer {
                    alpha = if (rotated) animateBack else animateFront
                }
        )

        Text(
            text = timerState.getCurrentStateMaxTime().toString(),
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(stateTimer) {
                    top.linkTo(totalTimer.bottom, 12.dp)
                    start.linkTo(totalTimer.start)
                    end.linkTo(totalTimer.end)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
                .graphicsLayer {
                    alpha = if (rotated) animateBack else animateFront
                }
        )

        Text(
            text = "${timerState.getWaterWeightCurrentState()} g",
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(stateWeight) {
                    bottom.linkTo(totalTimer.top, 12.dp)
                    start.linkTo(totalTimer.start)
                    end.linkTo(totalTimer.end)
                    width = Dimension.wrapContent
                    height = Dimension.wrapContent
                }
                .graphicsLayer {
                    alpha = if (rotated) animateBack else animateFront
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimerDisplay() {
    TimerDisplay(timerState = TimerState(60), emptyList()) {}
}