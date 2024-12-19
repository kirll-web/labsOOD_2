import MultiGumballMachine.*
import org.junit.jupiter.api.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

val mockMultiGm = object : IMultiGumballMachineImpl {
    override fun releaseBall() {}

    override fun getBallCount(): UInt = 0u

    override fun setSoldOutState() {}

    override fun setNoQuarterState() {}

    override fun setHasQuarterState() {}

    override fun setSoldState() {}
    override fun tryAddQuarter() = true

    override fun releaseQuarter() {}

    override fun tryEjectAllQuarter() = true

    override fun getCountQuarter() = 0u
    override fun addGumball(numBalls: UInt) {}
}

fun getMultiStateString(stateName: String): String = when (stateName) {
    HasQuarterState::class.java.name -> HasQuarterState(mockMultiGm).toString()
    SoldState::class.java.name -> SoldState(mockMultiGm).toString()
    NoQuarterState::class.java.name -> NoQuarterState(mockMultiGm).toString()
    SoldOutState::class.java.name -> SoldOutState(mockMultiGm).toString()
    else -> ""
}

fun getTemplateMultiGumballMachineString(gumballs: UInt, state: String, quarters: UInt) =
    "(\nMighty Gumball, Inc.C++ - enabled Standing Gumball Model #2016\n " +
            "Inventory : $gumballs gumball${if (gumballs != 1u) "s" else ""}\n" +
            "Machine is ${getMultiStateString(state)}\n" +
            "Quarted inserted: $quarters)"


class TestMultiGumballMachine {

    @Test
    @DisplayName("Тестирование функции toString после инициализации машины")
    fun testToStringInitGumballMachine() {
        val expectedResultNumberBalls = 5u
        val gm = MultiGumballMachine(expectedResultNumberBalls)
        val expectedResultToString = getTemplateMultiGumballMachineString(
            expectedResultNumberBalls,
            NoQuarterState::class.java.name,
            0u
        )


        assertEquals(gm.toString(), expectedResultToString)
        assertNotEquals(
            gm.toString(), getTemplateMultiGumballMachineString(
                7u,
                HasQuarterState::class.java.name,
                0u
            )
        )
        assertEquals(gm.toString(), expectedResultToString)
    }

    @Test
    @DisplayName("Тестирование заполнения автомата монетами")
    fun testInsertedQuarteds() {
        val expectedResultNumberBalls = 5u
        val gm = MultiGumballMachine(expectedResultNumberBalls)
        var expectedResultToString = getTemplateMultiGumballMachineString(
            expectedResultNumberBalls,
            HasQuarterState::class.java.name,
            0u
        )


        for (count in 1u ..  MultiGumballMachine.MAX_COUNT_QUARTER) {
            gm.insertQuarter()
            assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
                expectedResultNumberBalls,
                HasQuarterState::class.java.name,
                count
            ))
        }

        gm.insertQuarter()
        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            expectedResultNumberBalls,
            HasQuarterState::class.java.name,
            5u
        ))
        assertNotEquals(gm.toString(), getTemplateMultiGumballMachineString(
            expectedResultNumberBalls,
            HasQuarterState::class.java.name,
            6u
        ))
    }

    @Test
    @DisplayName("Тестирование заполнения автомата монетами, взятия жвачки и вставки ещё одной монеты")
    fun testInsertedQuartersAndGetGumball() {
        var expectedResultNumberBalls = 5u
        val gm = MultiGumballMachine(expectedResultNumberBalls)

        for (count in 1u ..  MultiGumballMachine.MAX_COUNT_QUARTER) {
            gm.insertQuarter()
        }

        gm.turnCrank()

        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            4u,
            HasQuarterState::class.java.name,
            4u
        ))

        gm.insertQuarter()

        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            4u,
            HasQuarterState::class.java.name,
            5u
        ))

        gm.insertQuarter()
        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            4u,
            HasQuarterState::class.java.name,
            5u
        ))
    }

    @Test
    @DisplayName("Тестирование автомата заполенения 2 монетами, получение 2 жвачек и возвращаения в состояния NoHasQuarter")
    fun testInsertedQuartersAndSoldOut() {
        val startGumball = 2u
        val startQuarter = 5u
        val gm = MultiGumballMachine(startGumball)

        for (count in 0u until startQuarter) {
            gm.insertQuarter()
        }

        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            startGumball,
            HasQuarterState::class.java.name,
            startQuarter
        ))

        for (count in 0u until startGumball) {
            gm.turnCrank()
        }

        val expectedGumball = 0u
        var expectedQuarter = 3u

        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            expectedGumball,
            SoldOutState::class.java.name,
            expectedQuarter
        ))

        gm.ejectQuarter()
        expectedQuarter = 0u
        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            expectedGumball,
            SoldOutState::class.java.name,
            expectedQuarter
        ))
    }


    @Test
    @DisplayName("Тестирование автомата заполнения 2 монетами, получение 2 жвачек и возвращаения в состояния NoHasQuarter")
    fun testInsertedQuartersAndNoQuarterState() {
        val startGumball = 5u
        val startQuarter = 2u
        val gm = MultiGumballMachine(startGumball)

        for (count in 0u until startQuarter) {
            gm.insertQuarter()
        }

        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            startGumball,
            HasQuarterState::class.java.name,
            startQuarter
        ))

        for (count in 0u until startQuarter) {
            gm.turnCrank()
        }

        val expectedGumball = 3u
        val expectedQuarter = 0u
        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            expectedGumball,
            NoQuarterState::class.java.name,
            expectedQuarter
        ))

        gm.ejectQuarter()
        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            expectedGumball,
            NoQuarterState::class.java.name,
            expectedQuarter
        ))
    }

    @Test
    @DisplayName("Тестирование автомата с доведением до SoldOut и заполнением жвачкой, монеты остаются")
    fun testSoldOutRefillAndHasQuarterState() {
        val startGumball = 2u
        val startQuarter = 5u
        val gm = MultiGumballMachine(startGumball)

        for (count in 0u until startQuarter) {
            gm.insertQuarter()
        }

        for (count in 0u until startGumball) {
            gm.turnCrank()
        }

        var expectedGumball = 0u
        var expectedQuarter = 3u

        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            expectedGumball,
            SoldOutState::class.java.name,
            expectedQuarter
        ))

        expectedGumball = 5u

        gm.refill(expectedGumball)
        val g = gm.toString()
        val g2 =  getTemplateMultiGumballMachineString(
            expectedGumball,
            HasQuarterState::class.java.name,
            expectedQuarter
        )
        assertEquals(g,g2)
    }

    @Test
    @DisplayName("Тестирование автомата c заполнением жвачкой в состоянии HasQuarter")
    fun testRefillInHasQuarterState() {
        val startGumball = 2u
        val startQuarter = 5u
        val gm = MultiGumballMachine(startGumball)

        for (count in 0u until startQuarter) {
            gm.insertQuarter()
        }

        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            startGumball,
            HasQuarterState::class.java.name,
            startQuarter
        ))

        val addGumball = 5u

        gm.refill(addGumball)
        val expectedGumball = startGumball + addGumball
        assertEquals(gm.toString(),getTemplateMultiGumballMachineString(
            expectedGumball,
            HasQuarterState::class.java.name,
            startQuarter
        ))
    }

    @Test
    @DisplayName("Тестирование автомата c заполнением жвачкой в состоянии NoQuarter")
    fun testRefillInNoQuarterState() {
        val startGumball = 2u
        val startQuarter = 5u
        val gm = MultiGumballMachine(startGumball)

        for (count in 0u until startQuarter) {
            gm.insertQuarter()
        }

        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            startGumball,
            NoQuarterState::class.java.name,
            startQuarter
        ))

        val addGumball = 5u

        gm.refill(addGumball)
        val expectedGumball = startGumball + addGumball
        assertEquals(gm.toString(),getTemplateMultiGumballMachineString(
            expectedGumball,
            NoQuarterState::class.java.name,
            startQuarter
        ))
    }

    @Test
    @DisplayName("Тестирование автомата c доведением до soldState и переходом в состояние NoQuarter")
    fun testSoldOutRefillInNoQuarterState() {
        val startGumball = 3u
        val startQuarter = startGumball
        val gm = MultiGumballMachine(startGumball)

        for (count in 0u until startQuarter) {
            gm.insertQuarter()
        }
        assertEquals(gm.toString(), getTemplateMultiGumballMachineString(
            startGumball,
            HasQuarterState::class.java.name,
            startQuarter
        ))


        for (count in 0u until startQuarter) {
            gm.turnCrank()
        }
        assertEquals(gm.toString(),getTemplateMultiGumballMachineString(
            0u,
            SoldOutState::class.java.name,
            0u
        ))


        val addGumball = 5u

        gm.refill(addGumball)
        val expectedGumball = addGumball
        assertEquals(gm.toString(),getTemplateMultiGumballMachineString(
            expectedGumball,
            NoQuarterState::class.java.name,
            0u
        ))
    }

}

