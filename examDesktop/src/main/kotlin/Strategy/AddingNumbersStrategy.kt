package Strategy

class AddingNumbersStrategy: IStrategy {
    override fun execute(firstNumber: Int, secondNumber: Int) = firstNumber + secondNumber
}