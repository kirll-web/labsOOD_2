interface IState {
    fun insertQuarter()
    fun ejectQuarter()
    fun turnCrank()
    fun dispense()
    fun refill(numBalls: UInt)
    override fun toString(): String
}