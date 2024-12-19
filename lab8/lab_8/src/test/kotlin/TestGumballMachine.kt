import StandartGumballMachineState.*
import org.junit.jupiter.api.DisplayName
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

val mockGm = object : IGumballMachineImpl {
    override fun releaseBall() {}

    override fun getBallCount(): UInt = 0u

    override fun setSoldOutState() {}

    override fun setNoQuarterState() {}

    override fun setHasQuarterState() {}

    override fun setSoldState() {}
}

fun getStateString(stateName: String): String = when (stateName) {
    HasQuarterState::class.java.name -> HasQuarterState(mockGm).toString()
    SoldState::class.java.name -> SoldState(mockGm).toString()
    NoQuarterState::class.java.name -> NoQuarterState(mockGm).toString()
    SoldOutState::class.java.name -> SoldOutState(mockGm).toString()
    else -> ""
}


fun getTemplateGumballMachineString(count: UInt, state: String) =
    "(\nMighty Gumball, Inc.C++ - enabled Standing Gumball Model #2016\n " +
            "Inventory : $count gumball${if (count != 1u) "s" else ""}\n" +
            "Machine is $state\n)"


fun isOutputEqualExpectedString(expectedStrings: List<String>, cb: () -> Unit) {
    val outputStream = ByteArrayOutputStream()
    val originalOut = System.out
    val expectedOutputStream = ByteArrayOutputStream()
    System.setOut(PrintStream(outputStream)) // Перенаправляем поток

    try {
        cb()
        val output = outputStream.toString().trim()
        System.setOut(PrintStream(expectedOutputStream))

        expectedStrings.forEach {
            println(it)
        }

        val expectedOutput = expectedOutputStream.toString().trim()
        assertEquals(output, expectedOutput)
    } finally {
        // Восстанавливаем стандартный поток вывода
        System.setOut(originalOut)
    }
}

class TestGumballMachine {

    @Test
    @DisplayName("Тестирование функции toString после инициализации машины")
    fun testToStringInitGumballMachine() {
        val expectedResultNumberBalls = 5u
        val gm = GumballMachineWithState(expectedResultNumberBalls)
        val expectedResultToString = getTemplateGumballMachineString(
            expectedResultNumberBalls,
            getStateString(NoQuarterState::class.java.name)
        )


        assertEquals(gm.toString(), expectedResultToString)
        assertNotEquals(
            gm.toString(), getTemplateGumballMachineString(
                7u,
                getStateString(HasQuarterState::class.java.name)
            )
        )
        assertEquals(gm.toString(), expectedResultToString)
    }

    //тестирование ввода монеты
    @Test
    @DisplayName("Тестирование ввода монеты")
    fun testInsertQuarterOutput() {
        val expectedGumballs = 5u
        val gm = GumballMachineWithState(expectedGumballs)
        val expectedState = getStateString(HasQuarterState::class.java.name)
        val expectedResultToString = getTemplateGumballMachineString(
            expectedGumballs,
            expectedState
        )

        gm.insertQuarter()

        assertEquals(gm.toString(), expectedResultToString)
    }

    @Test
    @DisplayName("Тестирование прокрутки в изначальном состоянии")
    fun testTurnCrankOutput() {
        val expectedGumballs = 5u
        val gm = GumballMachineWithState(expectedGumballs)
        val expectedState = getStateString(NoQuarterState::class.java.name)
        val expectedResultToString = getTemplateGumballMachineString(
            expectedGumballs,
            expectedState
        )
        assertEquals(gm.toString(), expectedResultToString)
    }

    @Test
    @DisplayName("Тестирование ввода и вывода монеты")
    fun testInsert2QuarterOutput() {
        val expectedGumballs = 5u
        val gm = GumballMachineWithState(expectedGumballs)
        val expectedStategAfterInsert =
            getStateString(HasQuarterState::class.java.name)
        val expectedResultToStringAfterInsert =
            getTemplateGumballMachineString(
                expectedGumballs,
                expectedStategAfterInsert
            )

        val expectedStategAfterEject =
            getStateString(NoQuarterState::class.java.name)
        val expectedResultToStringAfterEject =
            getTemplateGumballMachineString(
                expectedGumballs,
                expectedStategAfterEject
            )

        gm.insertQuarter()
        assertEquals(gm.toString(), expectedResultToStringAfterInsert)
        gm.ejectQuarter()
        assertEquals(gm.toString(), expectedResultToStringAfterEject)
    }

    @Test
    @DisplayName("Тестирование ввода монеты и поворота рычага")
    fun testInsertAndTurnCrankOutput() {
        var startQuarter = 5u
        val gm = GumballMachineWithState(startQuarter)
        val expectedStategAfterInsert =
            getStateString(HasQuarterState::class.java.name)

        val expectedGumballs = 4u
        val expectedStateAfterCrunk =
            getStateString(NoQuarterState::class.java.name)

        for (i in expectedGumballs downTo 1u) {
            val expectedResultToStringAfterInsert =
                getTemplateGumballMachineString(
                    startQuarter,
                    expectedStategAfterInsert
                )
            val expectedResultToStringAfterCrunk =
                getTemplateGumballMachineString(
                    i,
                    expectedStateAfterCrunk
                )

            gm.insertQuarter()
            assertEquals(gm.toString(), expectedResultToStringAfterInsert)
            gm.turnCrank()
            assertEquals(gm.toString(), expectedResultToStringAfterCrunk)

            startQuarter -= 1u
        }
    }


    @Test
    @DisplayName("Тестирование состояния после покупки последней жвачки sold out")
    fun testSoldOutAfterSoldOutput() {
        val startQuarter = 1u
        val gm = GumballMachineWithState(startQuarter)
        val expectedStateAfterInsert =
            getStateString(HasQuarterState::class.java.name)

        val expectedResultToStringAfterInsert =
            getTemplateGumballMachineString(
                startQuarter,
                expectedStateAfterInsert
            )

        val expectedGumballs = 0u
        val expectedStateAfterCrunk =
            getStateString(SoldOutState::class.java.name)

        val expectedResultToStringAfterCrunk =
            getTemplateGumballMachineString(
                expectedGumballs,
                expectedStateAfterCrunk
            )

        gm.insertQuarter()
        assertEquals(gm.toString(), expectedResultToStringAfterInsert)
        gm.turnCrank()
        assertEquals(gm.toString(), expectedResultToStringAfterCrunk)
    }

    @Test
    @DisplayName("Тестирование состояния sold out. Должны игнорироваться все действия")
    fun testSoldOutOutput() {
        val expectedGumballs = 0u
        val gm = GumballMachineWithState(expectedGumballs)
        val expectedStateAfterCrunk =
            getStateString(SoldOutState::class.java.name)

        val expectedResultToString = getTemplateGumballMachineString(
            expectedGumballs,
            expectedStateAfterCrunk
        )


        gm.insertQuarter()
        assertEquals(gm.toString(), expectedResultToString)
        gm.turnCrank()
        assertEquals(gm.toString(), expectedResultToString)
        gm.ejectQuarter()
        assertEquals(gm.toString(), expectedResultToString)
    }

    //todo классы состояний тоже протестировать
    @Test
    @DisplayName("Тестирование состояния SoldState")
    fun testSoldStateOutput() {
        val soldState = "SoldState"
        var state = soldState

        val soldOutState = "SoldOutState"
        val hasQuarterState = "HasNoQuarterState"
        val noHasQuarterState = "NoHasQuarterState"

        val startQuarter = 2u
        val expectedQuarterAfterFirstDispense = 1u

        val expectedQuarterAfterSecondDispense = 0u


        var quarter = startQuarter

        val mockGm = object : IGumballMachineImpl {
            override fun releaseBall() {
                --quarter
            }

            override fun getBallCount(): UInt = quarter

            override fun setSoldOutState() {
                state = soldOutState
            }

            override fun setNoQuarterState() {
                state = noHasQuarterState
            }

            override fun setHasQuarterState() {
                state = hasQuarterState
            }

            override fun setSoldState() {
                state = soldState
            }
        }


        val realSoldState = SoldState(mockGm)


        realSoldState.turnCrank()
        assertEquals(quarter == startQuarter, true)
        assertEquals(state == soldState, true)

        realSoldState.insertQuarter()
        assertEquals(quarter == startQuarter, true)
        assertEquals(state == soldState, true)

        realSoldState.ejectQuarter()
        assertEquals(quarter == startQuarter, true)
        assertEquals(state == soldState, true)

        realSoldState.dispense()
        assertEquals(quarter == expectedQuarterAfterFirstDispense, true)
        assertEquals(state == noHasQuarterState, true)

        realSoldState.dispense()
        assertEquals(quarter == expectedQuarterAfterSecondDispense, true)
        assertEquals(state == soldOutState, true)
    }

}

