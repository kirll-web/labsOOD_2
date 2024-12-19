import org.junit.jupiter.api.DisplayName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


fun getMultiNaiveStateString(stateName: MultiNaiveGumballMachine.State): String = when (stateName) {
    MultiNaiveGumballMachine.State.NoQuarter -> "waiting for quarter"
    MultiNaiveGumballMachine.State.HasQuarter -> "waiting for turn of crank"
    MultiNaiveGumballMachine.State.Sold ->  "delivering a gumball"
    MultiNaiveGumballMachine.State.SoldOut -> "sold out"
}

fun getTemplateMultiNaiveGumballMachineString(gumballs: UInt, state: MultiNaiveGumballMachine.State, quarters: UInt) =
    "(\nMighty Gumball, Inc.C++ - enabled Standing Gumball Model #2016\n " +
            "Inventory : $gumballs gumball${if (gumballs != 1u) "s" else ""}\n" +
            "Machine is ${getMultiNaiveStateString(state)}\n" +
            "Quarted inserted: $quarters)"


class TestMultiNaiveGumballMachine {

    @Test
    @DisplayName("Тестирование функции toString после инициализации машины")
    fun testToStringInitGumballMachine() {
        val expectedResultNumberBalls = 5u
        val gm = MultiNaiveGumballMachine(expectedResultNumberBalls)
        val expectedResultToString = getTemplateMultiNaiveGumballMachineString(
            expectedResultNumberBalls,
            MultiNaiveGumballMachine.State.NoQuarter,
            0u
        )


        assertEquals(gm.toString(), expectedResultToString)
        assertNotEquals(
            gm.toString(), getTemplateMultiNaiveGumballMachineString(
                7u,
                MultiNaiveGumballMachine.State.HasQuarter,
                0u
            )
        )
        assertEquals(gm.toString(), expectedResultToString)
    }

    @Test
    @DisplayName("Тестирование заполнения автомата монетами")
    fun testInsertedQuarteds() {
        val expectedResultNumberBalls = 5u
        val gm = MultiNaiveGumballMachine(expectedResultNumberBalls)
        var expectedResultToString = getTemplateMultiNaiveGumballMachineString(
            expectedResultNumberBalls,
            MultiNaiveGumballMachine.State.HasQuarter, 
            0u
        )


        for (count in 1u ..  MultiNaiveGumballMachine.MAX_COUNT_QUARTER) {
            gm.insertQuarter()
            assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
                expectedResultNumberBalls,
                MultiNaiveGumballMachine.State.HasQuarter,
                count
            ))
        }

        gm.insertQuarter()
        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            expectedResultNumberBalls,
            MultiNaiveGumballMachine.State.HasQuarter,
            5u
        ))
        assertNotEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            expectedResultNumberBalls,
            MultiNaiveGumballMachine.State.HasQuarter,
            6u
        ))
    }

    @Test
    @DisplayName("Тестирование заполнения автомата монетами, взятия жвачки и вставки ещё одной монеты")
    fun testInsertedQuartersAndGetGumball() {
        var expectedResultNumberBalls = 5u
        val gm = MultiNaiveGumballMachine(expectedResultNumberBalls)

        for (count in 1u ..  MultiNaiveGumballMachine.MAX_COUNT_QUARTER) {
            gm.insertQuarter()
        }

        gm.turnCrank()

        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            4u,
            MultiNaiveGumballMachine.State.HasQuarter,
            4u
        ))

        gm.insertQuarter()

        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            4u,
            MultiNaiveGumballMachine.State.HasQuarter,
            5u
        ))

        gm.insertQuarter()
        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            4u,
            MultiNaiveGumballMachine.State.HasQuarter,
            5u
        ))
    }

    @Test
    @DisplayName("Тестирование автомата заполенения 2 монетами, получение 2 жвачек и возвращаения в состояния NoHasQuarter")
    fun testInsertedQuartersAndSoldOut() {
        val startGumball = 2u
        val startQuarter = 5u
        val gm = MultiNaiveGumballMachine(startGumball)

        for (count in 0u until startQuarter) {
            gm.insertQuarter()
        }

        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            startGumball,
            MultiNaiveGumballMachine.State.HasQuarter,
            startQuarter
        ))

        for (count in 0u until startGumball) {
            gm.turnCrank()
        }

        val expectedGumball = 0u
        var expectedQuarter = 3u

        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            expectedGumball,
            MultiNaiveGumballMachine.State.SoldOut,
            expectedQuarter
        ))

        gm.ejectQuarter()
        expectedQuarter = 0u
        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            expectedGumball,
            MultiNaiveGumballMachine.State.SoldOut,
            expectedQuarter
        ))
    }


    @Test
    @DisplayName("Тестирование автомата заполнения 2 монетами, получение 2 жвачек и возвращаения в состояния NoHasQuarter")
    fun testInsertedQuartersAndNoQuarterState() {
        val startGumball = 5u
        val startQuarter = 2u
        val gm = MultiNaiveGumballMachine(startGumball)

        for (count in 0u until startQuarter) {
            gm.insertQuarter()
        }

        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            startGumball,
            MultiNaiveGumballMachine.State.HasQuarter,
            startQuarter
        ))

        for (count in 0u until startQuarter) {
            gm.turnCrank()
        }

        val expectedGumball = 3u
        val expectedQuarter = 0u
        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            expectedGumball,
            MultiNaiveGumballMachine.State.NoQuarter,
            expectedQuarter
        ))

        gm.ejectQuarter()
        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            expectedGumball,
            MultiNaiveGumballMachine.State.NoQuarter,
            expectedQuarter
        ))
    }


    @Test
    @DisplayName("Тестирование автомата с доведением до SoldOut и заполнением жвачкой, монеты остаются")
    fun testSoldOutRefillAndHasQuarterState() {
        val startGumball = 2u
        val startQuarter = 5u
        val gm = MultiNaiveGumballMachine(startGumball)

        for (count in 0u until startQuarter) {
            gm.insertQuarter()
        }

        for (count in 0u until startGumball) {
            gm.turnCrank()
        }

        var expectedGumball = 0u
        var expectedQuarter = 3u

        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            expectedGumball,
            MultiNaiveGumballMachine.State.SoldOut,
            expectedQuarter
        ))

        expectedGumball = 5u

        gm.refill(expectedGumball)
        val g = gm.toString()
        val g2 =  getTemplateMultiNaiveGumballMachineString(
            expectedGumball,
            MultiNaiveGumballMachine.State.HasQuarter,
            expectedQuarter
        )
        assertEquals(g,g2)
    }

    @Test
    @DisplayName("Тестирование автомата c заполнением жвачкой в состоянии HasQuarter")
    fun testRefillInHasQuarterState() {
        val startGumball = 2u
        val startQuarter = 5u
        val gm = MultiNaiveGumballMachine(startGumball)

        for (count in 0u until startQuarter) {
            gm.insertQuarter()
        }

        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            startGumball,
            MultiNaiveGumballMachine.State.HasQuarter,
            startQuarter
        ))

        val addGumball = 5u

        gm.refill(addGumball)
        val expectedGumball = startGumball + addGumball
        assertEquals(gm.toString(),getTemplateMultiNaiveGumballMachineString(
            expectedGumball,
            MultiNaiveGumballMachine.State.HasQuarter,
            startQuarter
        ))
    }

    @Test
    @DisplayName("Тестирование автомата c заполнением жвачкой в состоянии NoQuarter")
    fun testRefillInNoQuarterState() {
        val startGumball = 2u
        val startQuarter = 0u
        val gm = MultiNaiveGumballMachine(startGumball)

        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            startGumball,
            MultiNaiveGumballMachine.State.NoQuarter,
            startQuarter
        ))

        val addGumball = 5u

        gm.refill(addGumball)
        val expectedGumball = startGumball + addGumball
        assertEquals(gm.toString(),getTemplateMultiNaiveGumballMachineString(
            expectedGumball,
            MultiNaiveGumballMachine.State.NoQuarter,
            startQuarter
        ))
    }

    @Test
    @DisplayName("Тестирование автомата c доведением до soldState и переходом в состояние NoQuarter")
    fun testSoldOutRefillInNoQuarterState() {
        val startGumball = 3u
        val startQuarter = startGumball
        val gm = MultiNaiveGumballMachine(startGumball)

        for (count in 0u until startQuarter) {
            gm.insertQuarter()
        }
        assertEquals(gm.toString(), getTemplateMultiNaiveGumballMachineString(
            startGumball,
            MultiNaiveGumballMachine.State.HasQuarter,
            startQuarter
        ))


        for (count in 0u until startQuarter) {
            gm.turnCrank()
        }
        assertEquals(gm.toString(),getTemplateMultiNaiveGumballMachineString(
            0u,
            MultiNaiveGumballMachine.State.SoldOut,
            0u
        ))


        val addGumball = 5u

        gm.refill(addGumball)
        val expectedGumball = addGumball
        assertEquals(gm.toString(),getTemplateMultiNaiveGumballMachineString(
            expectedGumball,
            MultiNaiveGumballMachine.State.NoQuarter,
            0u
        ))
    }

}

