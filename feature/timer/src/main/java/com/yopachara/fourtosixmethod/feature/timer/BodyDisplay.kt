package com.yopachara.fourtosixmethod.feature.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.feature.timer.state.TimerDisplayState

@Composable
fun BodyDisplay(timerDisplayState: TimerDisplayState, changeBodyLevel: (Level) -> Unit) {

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {
        val mRememberObserver =
            remember<MutableState<Level>> { mutableStateOf(timerDisplayState.recipe.level) }

        Text(
            text = "Body",
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
        )

        Column(Modifier.padding()) {
            Row(Modifier.padding(top = 8.dp)) {
                RadioButton(
                    selected = mRememberObserver.value == Level.Basic,
                    onClick = {
                        mRememberObserver.value = Level.Basic
                        changeBodyLevel.invoke(mRememberObserver.value)
                    },
                    enabled = true,
                )
                Text(
                    text = "Basic",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }


            Row(Modifier.padding(vertical = 8.dp)) {
                RadioButton(
                    selected = mRememberObserver.value == Level.Week,
                    onClick = {
                        mRememberObserver.value = Level.Week
                        changeBodyLevel.invoke(mRememberObserver.value)
                    },
                    enabled = true,
                )
                Text(
                    text = "Week",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }


            Row {
                RadioButton(
                    selected = mRememberObserver.value == Level.Strong,
                    onClick = {
                        mRememberObserver.value = Level.Strong
                        changeBodyLevel.invoke(mRememberObserver.value)
                    },
                    enabled = true,
                )
                Text(
                    text = "Strong",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBodyDisplay() {
    BodyDisplay(timerDisplayState = TimerDisplayState()) {}
}