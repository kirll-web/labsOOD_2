package Strategy

class SubtractionNumbersStrategy: IStrategy {
    override fun execute(firstNumber: Int, secondNumber: Int) = firstNumber - secondNumber
}