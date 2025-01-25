package presenter

import kotlinx.coroutines.flow.StateFlow

interface ICalculatorObserver {
    fun update(firstNumber: Int, secondNumber: Int, result: Int)
}

interface ICalculatorPresenter {
    val state: StateFlow<IStateView>
    fun sendUIEvent(event: UIEvent)
}


interface IStateView {
    val firstNumber: String
    val secondNumber: String
    val result: String
}

sealed interface UIEvent {
    data class UpdateFirstNumber(val value: String): UIEvent
    data class UpdateSecondNumber(val value: String): UIEvent
}

