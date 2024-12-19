package StandartGumballMachineState

class SoldState(
    private val gumballMachine: IGumballMachineImpl
) : IState {
    override fun insertQuarter() {
        println("Please wait, we're already giving you a gumball")
    }

    override fun ejectQuarter() {
        println("Sorry you already turned the crank")
    }

    override fun turnCrank() {
        println("Turning twice doesn't get you another gumball")
    }

    override fun dispense() {
        gumballMachine.releaseBall()
        if (gumballMachine.getBallCount() == 0u) {
            println("Oops, out of gumballs")
            gumballMachine.setSoldOutState()
        } else {
            gumballMachine.setNoQuarterState()
        }
    }

    override fun toString(): String {
        return "delivering a gumball"
    }
}

class SoldOutState(
    private val gumballMachine: IGumballMachineImpl
) : IState {

    override fun insertQuarter() {
        println("You can't insert a quarter, the machine is sold out")
    }

    override fun ejectQuarter() {
        println("You can't eject, you haven't inserted a quarter yet")
    }

    override fun turnCrank() {
        println("You turned but there's no gumballs")
    }

    override fun dispense() {
        println("No gumball dispensed")
    }

    override fun toString() = "sold out"
}

class HasQuarterState(
    private val gumballMachine: IGumballMachineImpl
) : IState {
    override fun insertQuarter() {
        println("You can't insert another quarter")
    }

    override fun ejectQuarter() {
        println("Quarter returned")
        gumballMachine.setNoQuarterState()
    }

    override fun turnCrank() {
        println("You turned...")
        gumballMachine.setSoldState()
    }

    override fun dispense() {
        println("No gumball dispensed")
    }

    override fun toString() = "No gumball dispensed"

}

class NoQuarterState(
    private val gumballMachine: IGumballMachineImpl
) : IState {

    override fun insertQuarter() {
        println("You inserted a quarter")
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

    override fun toString() = "waiting for quarter"
}

class GumballMachineWithState(
    numBalls: UInt
) : IGumballMachine {
    private val ruleState: IGumballMachineImpl = object: IGumballMachineImpl {
        override fun getBallCount(): UInt = mCount
        override fun releaseBall() {
            if (mCount != 0u) {
                println("A gumball comes rolling out the slot...")
                mCount -= 1u
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
    }

    private var mCount = numBalls
    private val mSoldState = SoldState(ruleState)
    private val mSoldOutState = SoldOutState(ruleState)
    private val mNoQuarterState = NoQuarterState(ruleState)
    private val mHasQuarterState = HasQuarterState(ruleState)
    private var mState: IState = mSoldOutState

    init {
        if (mCount > 0u) {
            mState = mNoQuarterState
        }
    }

    override fun ejectQuarter() {
        mState.ejectQuarter()
    }

    override fun insertQuarter() {
        mState.insertQuarter()
    }

    override fun turnCrank() {
        mState.turnCrank()
        mState.dispense()
    }

    override fun toString() =
        "(\nMighty Gumball, Inc.C++ - enabled Standing Gumball Model #2016\n " +
                "Inventory : $mCount gumball${if (mCount != 1u) "s" else ""}\n" +
                "Machine is $mState\n)"

}