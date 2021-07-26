package com.yopachara.fourtosixmethod.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yopachara.fourtosixmethod.data.Balance
import com.yopachara.fourtosixmethod.data.TimerState
import com.yopachara.fourtosixmethod.ui.theme.Purple200

@Composable
fun BalanceDisplay(timerState: TimerState, changeBalanceLevel: (Balance) -> Unit) {

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {
        val mRememberObserver = remember { mutableStateOf(timerState.balance) }

        Text(
            text = "Balance",
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
        )

        Column(Modifier.padding()) {
            Row(Modifier.padding(top = 8.dp)) {
                RadioButton(
                    selected = mRememberObserver.value == Balance.Basic,
                    onClick = {
                        mRememberObserver.value = Balance.Basic
                        changeBalanceLevel.invoke(mRememberObserver.value)
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
                    selected = mRememberObserver.value == Balance.Sweet,
                    onClick = {
                        mRememberObserver.value = Balance.Sweet
                        changeBalanceLevel.invoke(mRememberObserver.value)
                    },
                    enabled = true,
                    colors = RadioButtonDefaults.colors(selectedColor = Purple200)
                )
                Text(
                    text = "Sweet",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }


            Row {
                RadioButton(
                    selected = mRememberObserver.value == Balance.Acid,
                    onClick = {
                        mRememberObserver.value = Balance.Acid
                        changeBalanceLevel.invoke(mRememberObserver.value)
                    },
                    enabled = true,
                    colors = RadioButtonDefaults.colors(selectedColor = Purple200)
                )
                Text(
                    text = "Acid",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBalanceDisplay() {
    BalanceDisplay(timerState = TimerState()) {}
}