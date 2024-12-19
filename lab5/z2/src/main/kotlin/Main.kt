package org.command

class MenuHelpCommand(
    private val menu: Menu
) : ICommand {
    override fun execute() {
        menu.showInstructions()
    }
}

class CExitMenuCommand(
    private val menu: Menu
) : ICommand {
    override fun execute() {
        menu.exit()
    }
}

fun MenuFP.createMacroCommand(commands: List<Command>): () -> Unit {
    return {
        commands.forEach { command ->
            command()
        }
    }
}

fun TestMenuWithClassicCommandPattern() {
    val robot = Robot()

    val menu = Menu()
    menu.addItem(
        "on", "Turns the Robot on", TurnOnCommand(robot, menu)
    )
    menu.addItem(
        "off", "Turns the Robot off", TurnOffCommand(robot, menu)
    )

    menu.addItem(
        "north", "Makes the Robot walk north", WalkCommand(robot, WalkDirection.North, menu)
    )
    menu.addItem(
        "south", "Makes the Robot walk south", WalkCommand(robot, WalkDirection.South, menu)
    )
    menu.addItem(
        "west", "Makes the Robot walk west", WalkCommand(robot, WalkDirection.West, menu)
    )
    menu.addItem(
        "east", "Makes the Robot walk east", WalkCommand(robot, WalkDirection.East, menu)
    )

    val cmd = MacroCommand()
    cmd.addCommand(TurnOnCommand(robot, menu))
    cmd.addCommand(WalkCommand(robot, WalkDirection.North, menu))
    cmd.addCommand(WalkCommand(robot, WalkDirection.East, menu))
    cmd.addCommand(WalkCommand(robot, WalkDirection.South, menu))
    cmd.addCommand(WalkCommand(robot, WalkDirection.West, menu))
    cmd.addCommand(TurnOffCommand(robot, menu))
    menu.addItem("patrol", "Patrol the territory", cmd)

    menu.addItem(
        "stop", "Stops the Robot",
        StopCommand(robot, menu)
    )

    menu.addItem(
        "help", "Show instructions",
        MenuHelpCommand(menu)
    )

    menu.addItem(
        "begin_macro", "Start record macro",
        BeginMacro(menu)
    )

    menu.addItem(
        "end_macro", "Stop record macro",
        EndMacro(menu)
    )

    menu.addItem(
        "exit", "Exit from this menu",
        ExitMenuCommand(menu)
    )

    menu.run()
}

fun TestMenuWithFunctionalCommandPattern() {
    val robot = Robot()
    val menu = MenuFP()

    fun recordMacro(cmd: Command) {
        if (menu.isRecordMacro())
        {
            menu.addCommandToCurrentMacro(cmd);
        }
    }

    menu.addItem("on", "Turns the Robot on") {
        if(menu.isRecordMacro()) recordMacro { robot.turnOn() }
        else robot.turnOn()
    }
    menu.addItem(
        "off", "Turns the Robot off"
    ) {
        if(menu.isRecordMacro()) recordMacro { robot.turnOff() }
        else robot.turnOff()
    }

    menu.addItem(
        "north", "Makes the Robot walk north"
    ) {
        if(menu.isRecordMacro()) recordMacro { robot.walk(WalkDirection.North) }
        else robot.walk(WalkDirection.North)
    }
    menu.addItem(
        "south", "Makes the Robot walk south"
    ) {
        if(menu.isRecordMacro()) recordMacro { robot.walk(WalkDirection.South) }
        else robot.walk(WalkDirection.South)
    }
    menu.addItem(
        "west", "Makes the Robot walk west"
    ) {
        if(menu.isRecordMacro()) recordMacro { robot.walk(WalkDirection.West) }
        else robot.walk(WalkDirection.West)
    }
    menu.addItem(
        "east", "Makes the Robot walk east"
    ) {
        if(menu.isRecordMacro()) recordMacro { robot.walk(WalkDirection.East) }
        else robot.walk(WalkDirection.East)
    }


    menu.addItem(
        "stop", "Stops the Robot"
    ) {
        if(menu.isRecordMacro()) recordMacro { robot.stop() }
        else robot.stop()
    }

    menu.addItem(
        "end_macro", "Stop record macro",
    ) {
        if (!menu.isRecordMacro()) {
            throw IllegalArgumentException("Macro recording is not in progress right now")
        }

        menu.addCurrentMacroMenuItem();
        println("Macro saved")
    }

    menu.addItem(
        "begin_macro", "Start record macro",
    ) {
        if (menu.isRecordMacro()) {
            throw IllegalArgumentException("Already recording a macro.")
        }

        print("Name: ")
        val name = readln()
        println()
        println("Description: ")
        val description = readln()
        menu.setCurrentMacro(MacroCommandFP(
            name, description
        ))
        println("Recording a new macro $name:")
    }

    menu.addItem(
        "patrol",
        "Patrol the territory",
    ) {
        if(menu.isRecordMacro()) recordMacro(
            menu.createMacroCommand(listOf(
                { robot.turnOn() },
                { robot.walk(WalkDirection.North) },
                { robot.walk(WalkDirection.South) },
                { robot.walk(WalkDirection.West) },

                { robot.walk(WalkDirection.East) },
                { robot.turnOff() }
            ))
        )
        else menu.createMacroCommand(listOf(
            { robot.turnOn() },
            { robot.walk(WalkDirection.North) },
            { robot.walk(WalkDirection.South) },
            { robot.walk(WalkDirection.West) },

            { robot.walk(WalkDirection.East) },
            { robot.turnOff() }
        ))()
    }

    menu.addItem(
        "help", "Show instructions") {
        menu.showInstructions()
    }
    menu.addItem(
        "exit", "Exit from this menu") {
        menu.exit()
    }

    menu.run()
}

fun main() {
    val menu = MenuFP()
    menu.addItem("c", "Classic command pattern implementation") {
        TestMenuWithClassicCommandPattern()
        menu.showInstructions()
    }
    menu.addItem("f", "Functional command pattern implementation") {
        TestMenuWithFunctionalCommandPattern()
        menu.showInstructions()
    }

    menu.addItem("q", "Exit Program") {
        menu.exit()
    }
    menu.run()

}