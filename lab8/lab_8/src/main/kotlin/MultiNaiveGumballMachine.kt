import MultiGumballMachine.MultiGumballMachine.Companion.MAX_COUNT_QUARTER
import MultiGumballMachine.MultiGumballMachine.Companion.MIN_COUNT_QUARTER

class MultiNaiveGumballMachine(
    private var countGumball: UInt
) : IGumballMachine {
    private var mCountQuarter = 0u
    private var mState = when {
        countGumball > 0u -> State.NoQuarter
        else -> State.SoldOut
    }

    enum class State {
        SoldOut, // Жвачка закончилась
        NoQuarter, // Нет монетки
        HasQuarter, // Есть монетка
        Sold, // Монетка выдана
    }

    override fun insertQuarter() {
        when (mState) {
            State.SoldOut -> println("You can't insert a quarter, the machine is sold out\n")
            State.NoQuarter -> {
                println("You inserted a quarter\n")
                tryAddQuarter()
                mState = State.HasQuarter
            }

            State.HasQuarter -> when {
                tryAddQuarter() -> println("You inserted a quarter")
                else -> println("You can't insert another quarter")
            }

            State.Sold -> println("Please wait, we're already giving you a gumball\n")
        }
    }

    override fun ejectQuarter() {
        when (mState) {
            State.HasQuarter -> {
                println("Quarter returned")
                tryEjectAllQuarter()
                if (mCountQuarter == 0u) mState = State.NoQuarter
            }

            State.NoQuarter -> println("You haven't inserted a quarter\n")
            State.Sold -> {
                if (!tryEjectAllQuarter()) {
                    println("Sorry you already turned the crank")
                } else {
                    println("Quarters returned")
                }
            }

            State.SoldOut -> {
                if (!tryEjectAllQuarter()) {
                    println("You can't eject, you haven't inserted a quarter yet")
                } else {
                    println("Quarters returned")
                }
            }
        }
    }

    override fun turnCrank() {
        when (mState) {
            State.SoldOut -> println("You turned but there's no gumballs\n")
            State.NoQuarter -> println("You turned but there's no quarter\n")
            State.HasQuarter -> {
                println("You turned...\n")
                mState = State.Sold
                dispense()
            }

            State.Sold -> println("Turning twice doesn't get you another gumball\n")
        }
    }

    override fun refill(numBalls: UInt) {
        when (mState) {
            State.SoldOut,
            State.NoQuarter,
            State.HasQuarter -> addGumballs(numBalls)

            State.Sold -> println("You can't add gumballs while giving out gumball")
        }
    }

    override fun toString(): String {
        val state = mState

        val str = when (state) {
            State.SoldOut -> "sold out"
            State.NoQuarter -> "waiting for quarter"
            State.HasQuarter -> "waiting for turn of crank"
            else -> "delivering a gumball"
        }

        return "(\nMighty Gumball, Inc.C++ - enabled Standing Gumball Model #2016\n " +
                "Inventory : $countGumball gumball${if (countGumball != 1u) "s" else ""}\n" +
                "Machine is $str\n" +
                "Quarted inserted: $mCountQuarter)"
    }

    private fun addGumballs(numBalls: UInt) {
        countGumball += numBalls
        mState = when {
            countGumball > 0u -> {
                if (mCountQuarter > MIN_COUNT_QUARTER) State.HasQuarter
                else State.NoQuarter
            }
            else -> State.SoldOut
        }
    }

    private fun tryAddQuarter(): Boolean {
        return when {
            mCountQuarter < MAX_COUNT_QUARTER -> {
                ++mCountQuarter
                true
            }

            else -> false
        }
    }

    private fun tryEjectAllQuarter() = when {
        mCountQuarter > 0u -> {
            mCountQuarter = 0u
            true
        }

        else -> false
    }

    private fun releaseQuarter() {
        if (mCountQuarter > MIN_COUNT_QUARTER) --mCountQuarter
        if (mCountQuarter == MIN_COUNT_QUARTER) mState = State.NoQuarter
    }

    private fun dispense() {
        when (mState) {
            State.Sold -> {
                println("A gumball comes rolling out the slot\n")
                countGumball -= 1u

                releaseQuarter()
                if (countGumball == 0u) {
                    println("Oops, out of gumballs")
                    mState = State.SoldOut
                } else {
                    when {
                        mCountQuarter > MIN_COUNT_QUARTER -> mState = State.HasQuarter
                        else -> State.NoQuarter
                    }
                }
            }

            State.NoQuarter -> println("You need to pay first\n")
            State.SoldOut,
            State.HasQuarter -> println("No gumball dispensed\n")
        }
    }

    companion object {
        const val MAX_COUNT_QUARTER = 5u
        const val MIN_COUNT_QUARTER = 0u
    }
}