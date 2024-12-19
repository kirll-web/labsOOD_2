package StandartGumballMachineState

interface IGumballMachineImpl {
    fun releaseBall()
    fun getBallCount(): UInt

    fun setSoldOutState() // товар закончился
    fun setNoQuarterState() // состояние нет монетки
    fun setHasQuarterState() // состояние есть монетка
    fun setSoldState()  // выдача товара
}
