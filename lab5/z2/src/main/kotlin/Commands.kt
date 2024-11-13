package org.command

class TurnOnCommand(
    private val robot: Robot,
    private val menu: Menu
) : ICommand {

    override fun execute() {
        if (menu.isRecordMacro()) {
            RecordCommand(this, menu).execute()
        } else robot.turnOn()
    }
}

class TurnOffCommand(
    private val robot: Robot,
    private val menu: Menu
) : ICommand {
    override fun execute() {
        if (menu.isRecordMacro()) {
            RecordCommand(this, menu).execute()
        } else robot.turnOff()
    }
}

class WalkCommand(
    private val robot: Robot,
    private val mDirection: WalkDirection,
    private val menu: Menu
) : ICommand {
    override fun execute() {
        if (menu.isRecordMacro()) {
            RecordCommand(this, menu).execute()
        } else robot.walk(mDirection)
    }

}

class StopCommand(
    private val robot: Robot,
    private val menu: Menu
) : ICommand {
    override fun execute() {
        if (menu.isRecordMacro()) {
            RecordCommand(this, menu).execute()
        } else robot.stop()
    }
}

class RecordCommand(
    private val command: ICommand,
    private val menu: Menu
): ICommand {
    override fun execute() {
        if (menu.isRecordMacro()) {
            menu.addCommandToCurrentMacro(command);
        }
    }
}

class MacroCommand(
    private val shortcut: String = "",
    private val description: String = ""
) : ICommand {
    private var mCommands = listOf<ICommand>()

    override fun execute() {
        mCommands.forEach { cmd ->
            cmd.execute()
        }
    }

    fun addCommand(cmd: ICommand) {
        mCommands = mCommands.plus(cmd)
    }

    fun getName() = shortcut
    fun getDescription() = description
}


class ExitMenuCommand(
    private val menu: Menu
) : ICommand {
    override fun execute() {
        if (menu.isRecordMacro()) {
            RecordCommand(this, menu).execute()
        } else menu.exit()
    }
}

class BeginMacro(
    private val menu: Menu
): ICommand {

    override fun execute() {
        if (menu.isRecordMacro()) {
            throw IllegalArgumentException("Already recording a macro.")
        }

        print("Name: ")
        val name = readln()
        println()
        println("Description: ")
        val description = readln()
        menu.setCurrentMacro(MacroCommand(
            name, description
        ))
        println("Recording a new macro $name:")
    }
}

class EndMacro(
    private val menu: Menu
): ICommand {

    override fun execute() {
        if (!menu.isRecordMacro()) {
            throw IllegalArgumentException("Macro recording is not in progress right now")
        }

        menu.addCurrentMacroMenuItem();
        println("Macro saved")
    }
}