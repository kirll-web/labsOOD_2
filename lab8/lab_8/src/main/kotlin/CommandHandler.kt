import Menu.FPCommand
import Menu.Menu
import Menu.MenuItem
import MultiGumballMachine.MultiGumballMachine


class CommandHandler(
    private val menu: Menu,
    private val gumballMachine: MultiGumballMachine,
) {
    init {
        addMenuItem("InsertQuarter", "Insert quarter") { _ ->
            gumballMachine.insertQuarter()
        }

        addMenuItem("EjectQuarter", "Eject quarter") { _ ->
            gumballMachine.ejectQuarter()
        }

        addMenuItem("TurnCrank", "Turn Crank") { _ ->
            gumballMachine.turnCrank()
        }

        addMenuItem("ToString", "output state of gumball machine ") { _ ->
            println(gumballMachine.toString())
        }

        addMenuItem("Refill", "Refill <num gumballs>") { input ->
            refill(input)
        }

    }

    fun run() {
        menu.showMenu()
        menu.run()
    }

    private fun addMenuItem(shortcut: String, descr: String, command: FPCommand) {
        menu.addItem(MenuItem(
            shortcut, descr, command
        ))
    }

    private fun refill(input: String) {
        val words = input.split(" ")

        if(words.count() != 1) {
            println("Incorrect command: Refill <num gumballs>")
        }

        val number = try {
            words[0].toUInt()
        } catch (ex: Exception) {
            println("It's not a number")
            return
        }

        gumballMachine.refill(number)
    }

}