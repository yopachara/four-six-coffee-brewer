package com.yopachara.fourtosixmethod.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yopachara.fourtosixmethod.core.data.model.Level
import com.yopachara.fourtosixmethod.core.database.model.TimerState
import com.yopachara.fourtosixmethod.core.designsystem.theme.Purple200

@Composable
fun BodyDisplay(timerState: TimerState, changeBodyLevel: (Level) -> Unit) {

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {
        val mRememberObserver =
            remember<MutableState<Level>> { mutableStateOf(timerState.recipe.level) }

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
                    colors = RadioButtonDefaults.colors(selectedColor = Purple200)
                )
                Text(
                    text = "Basic",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
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
                    colors = RadioButtonDefaults.colors(selectedColor = Purple200)
                )
                Text(
                    text = "Week",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
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
                    colors = RadioButtonDefaults.colors(selectedColor = Purple200)
                )
                Text(
                    text = "Strong",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBodyDisplay() {
    BodyDisplay(timerState = TimerState()) {}
}