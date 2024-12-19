package StandartGumballMachineState

interface IState {
    fun insertQuarter()
    fun ejectQuarter()
    fun turnCrank()
    fun dispense()
    override fun toString(): String
}