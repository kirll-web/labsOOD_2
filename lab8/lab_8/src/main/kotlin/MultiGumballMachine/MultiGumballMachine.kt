package MultiGumballMachine

import IGumballMachine
import IState

class SoldState(
    private val gumballMachine: IMultiGumballMachineImpl
) : IState {
    override fun insertQuarter() {
        println("Please wait, we're already giving you a gumball")
    }

    override fun ejectQuarter() {
       if(!gumballMachine.tryEjectAllQuarter()) {
           println("Sorry you already turned the crank")
       } else {
           println("Quarters returned")
       }
    }

    override fun turnCrank() {
        println("Turning twice doesn't get you another gumball")
    }

    override fun dispense() = with(gumballMachine){
        releaseBall()
        releaseQuarter()
        if (getBallCount() == 0u) {
            println("Oops, out of gumballs")
            setSoldOutState()
        } else {
            when {
                getCountQuarter() > MultiGumballMachine.MIN_COUNT_QUARTER ->
                    setHasQuarterState()
                else -> setNoQuarterState()
            }
        }
    }

    override fun refill(numBalls: UInt) {
        println("You can't add gumballs while giving out gumball")
    }

    override fun toString(): String {
        return "delivering a gumball"
    }
}

class SoldOutState(
    private val gumballMachine: IMultiGumballMachineImpl
) : IState {

    override fun insertQuarter() {
        println("You can't insert a quarter, the machine is sold out")
    }

    override fun ejectQuarter() {
        if(!gumballMachine.tryEjectAllQuarter()) {
            println("You can't eject, you haven't inserted a quarter yet")
        } else {
            println("Quarters returned")
        }
    }

    override fun turnCrank() {
        println("You turned but there's no gumballs")
    }

    override fun dispense() {
        println("No gumball dispensed")
    }

    override fun refill(numBalls: UInt) = with(gumballMachine){
        println("Added gumballs")
        addGumball(numBalls)

        if(getBallCount() > 0u) {
            if (getCountQuarter() > MultiGumballMachine.MIN_COUNT_QUARTER) {
                setHasQuarterState()
            }
            else {
                setNoQuarterState()
            }
        }
    }

    override fun toString() = "sold out"
}

class HasQuarterState(
    private val gumballMachine: IMultiGumballMachineImpl
) : IState {
    override fun insertQuarter() {
        when {
            gumballMachine.tryAddQuarter() -> println("You inserted a quarter")
            else -> println("You can't insert another quarter")
        }
    }

    override fun ejectQuarter() {
        println("Quarter returned")
        gumballMachine.tryEjectAllQuarter()
        if(gumballMachine.getCountQuarter() == 0u) gumballMachine.setNoQuarterState()
    }

    override fun turnCrank() {
        println("You turned...")
        gumballMachine.setSoldState()
    }

    override fun dispense() {
        println("No gumball dispensed")
    }

    override fun refill(numBalls: UInt) {
        println("Added gumballs")
        gumballMachine.addGumball(numBalls)
    }

    override fun toString() = "No gumball dispensed"

}

class NoQuarterState(
    private val gumballMachine: IMultiGumballMachineImpl
) : IState {

    override fun insertQuarter() {
        println("You inserted a quarter")
        gumballMachine.tryAddQuarter()
        gumballMachine.setHasQuarterState()
    }

    override fun ejectQuarter() {
        println("You haven't inserted a quarter")
    }

    override fun turnCrank() {
        println("You turned but there's no quarter")
    }

    override fun dispense() {
        println("You need to pay first")
    }

    override fun refill(numBalls: UInt) {
        println("Added gumballs")
        gumballMachine.addGumball(numBalls)
    }

    override fun toString() = "waiting for quarter"
}

class MultiGumballMachine(
    numBalls: UInt
) : IGumballMachine {
    private val mMultiGumballMachineImpl: IMultiGumballMachineImpl = object: IMultiGumballMachineImpl {
        override fun getBallCount(): UInt = mCountGumball
        override fun releaseBall() {
            if (mCountGumball > 0u) {
                println("A gumball comes rolling out the slot...")
                mCountGumball -= 1u
            }
        }

        override fun setSoldOutState() {
            mState = mSoldOutState
        }

        override fun setNoQuarterState() {
            mState = mNoQuarterState
        }

        override fun setSoldState() {
            mState = mSoldState
        }

        override fun setHasQuarterState() {
            mState = mHasQuarterState
        }

        override fun tryAddQuarter(): Boolean {
            return when {
                mCountQuarter < MAX_COUNT_QUARTER -> {
                    ++mCountQuarter
                    true
                }
                else -> false
            }
        }

        override fun releaseQuarter() {
            if (mCountQuarter > MIN_COUNT_QUARTER) --mCountQuarter
            if (mCountQuarter == MIN_COUNT_QUARTER) setNoQuarterState()
        }

        override fun tryEjectAllQuarter() = when {
            mCountQuarter > 0u -> {
                mCountQuarter = 0u
                true
            }
            else -> false
        }

        //todo переименовать на QuarterCount
        override fun getCountQuarter() = mCountQuarter
        override fun addGumball(numBalls: UInt) {
            mCountGumball += numBalls
        }
    }

    private var mCountGumball = numBalls
    private var mCountQuarter = MIN_COUNT_QUARTER //fixme поменял здесь
    private val mSoldState = SoldState(mMultiGumballMachineImpl)
    private val mSoldOutState = SoldOutState(mMultiGumballMachineImpl)
    private val mNoQuarterState = NoQuarterState(mMultiGumballMachineImpl)
    private val mHasQuarterState = HasQuarterState(mMultiGumballMachineImpl)
    private var mState: IState = mSoldOutState

    init {
        if (mCountGumball > 0u) {
            mState = mNoQuarterState
        }
    }

    override fun ejectQuarter() {
        mState.ejectQuarter()
    }

    override fun refill(numBalls: UInt) {
        mState.refill(numBalls)
    }

    override fun insertQuarter() {
        mState.insertQuarter()
    }

    override fun turnCrank() {
        mState.turnCrank()
        mState.dispense()
    }


    override fun toString() =
        "(\nMighty Gumball, Inc.Kotlin - enabled Standing Gumball Model #2024\n" +
                "Inventory : $mCountGumball gumball${if (mCountGumball != 1u) "s" else ""}\n" +
                "Machine is $mState\n" +
                "Quarted inserted: $mCountQuarter\n)"

    companion object {
        const val MAX_COUNT_QUARTER = 5u
        const val MIN_COUNT_QUARTER = 0u
    }
}