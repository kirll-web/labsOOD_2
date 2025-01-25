package presenter

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import model.IModel
import java.io.Closeable


data class StateView(
    override val firstNumber: String,
    override val secondNumber: String,
    override val result: String
): IStateView

class CalculatorPresenter(
    private val model: IModel
) : ICalculatorPresenter, ICalculatorObserver, Closeable {
    private val mState = MutableStateFlow<IStateView>(
        StateView("0", "0", "0")
    )
    override val state: StateFlow<IStateView> = mState

    init {
        model.registerObserver(this)
    }

    override fun sendUIEvent(event: UIEvent) {
        when (event) {
            is UIEvent.UpdateFirstNumber -> {
                val number = try {
                    event.value.toInt()
                } catch (ex: Exception) {
                    when {
                        event.value == "-" -> -0
                        event.value.isEmpty() -> 0
                        else -> return
                    }
                }
                model.updateFirstNumber(number)
            }

            is UIEvent.UpdateSecondNumber -> {
                val number = try {
                    event.value.toInt()
                } catch (ex: Exception) {
                    when {
                        event.value.isEmpty() -> 0
                        else -> return
                    }
                }
                model.updateSecondNumber(number)
            }
        }
    }

    override fun update(firstNumber: Int, secondNumber: Int, result: Int) {
        mState.value = StateView(
            prepareNumber(firstNumber),
            prepareNumber(secondNumber),
            prepareNumber(result)
        )
    }

    override fun close() {
        model.unegisterObserver(this)
    }

    private fun prepareNumber(number: Int) = when(number) {
        0 -> ""
        else -> number.toString()
    }
}