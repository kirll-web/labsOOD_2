package view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import presenter.ICalculatorPresenter
import presenter.UIEvent

@Composable
fun CalculatorView(
    presenter: ICalculatorPresenter
) {
    val state by presenter.state.collectAsState()
    Column {
        TextField(
            value = state.firstNumber,
            placeholder = {
                Text("0")
            },
            onValueChange = {
                presenter.sendUIEvent(UIEvent.UpdateFirstNumber(it))
            }
        )

        TextField(
            value = state.secondNumber,
            placeholder = {
                Text("0")
            },
            onValueChange = {
                presenter.sendUIEvent(UIEvent.UpdateSecondNumber(it))
            }
        )

        Text("Sum: ${state.result}")
    }
}