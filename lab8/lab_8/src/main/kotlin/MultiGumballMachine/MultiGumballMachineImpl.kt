package MultiGumballMachine

interface IMultiGumballMachineImpl {
    fun releaseBall()
    fun getBallCount(): UInt

    fun setSoldOutState() // товар закончился
    fun setNoQuarterState() // состояние нет монетки
    fun setHasQuarterState() // состояние есть монетка
    fun setSoldState()  // выдача товара
    fun tryAddQuarter(): Boolean
    fun releaseQuarter()
    fun tryEjectAllQuarter(): Boolean
    fun getCountQuarter(): UInt
    fun addGumball(numBalls: UInt)
}