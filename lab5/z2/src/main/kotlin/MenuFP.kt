package org.command

typealias Command = () -> Unit

class MenuFP {
    private var mCurrentMacro: MacroCommandFP? = null
    private var mMacros = listOf<MacroCommandFP>()
    var mItems = listOf<Item>()
    var mExit = false;

    data class Item(
        val shortcut: String,
        val description: String,
        val command: Command
    )

    fun addItem(
        shortcut: String,
        description: String,
        command: Command
    ) {
        mItems = mItems.plus(
            Item(
                shortcut,
                description,
                command
            )
        )
    }

    fun run() {
        showInstructions();

        var command: String
        while (!mExit) {
            println(">")
            command = readln()
            executeCommand(command)
        }
    }

    fun showInstructions() {
        println("Commands list:")
        mItems.forEach { item ->
            println("  ${item.shortcut}: ${item.description}")
        }
    }

    fun setCurrentMacro(currentMacros: MacroCommandFP) {
        when {
            mItems.find { it.shortcut == currentMacros.getName() } == null -> mCurrentMacro =
                currentMacros

            else -> throw IllegalArgumentException("This macro is already exists")
        }
    }

    fun addCommandToCurrentMacro(command: Command) {
        mCurrentMacro?.addCommand(command)
    }

    fun isRecordMacro() = mCurrentMacro != null

    fun addCurrentMacroMenuItem() {
        if (isRecordMacro()) {
            val copyMacro = mCurrentMacro

            copyMacro?.let { copy ->
                addItem(copy.getName(), copy.getDescription(), copyMacro::execute)
                mMacros = mMacros.plus(copyMacro)
            }

            mCurrentMacro = null
        }
    }

    fun exit() {
        mExit = true;
    }

    private fun executeCommand(command: String): Boolean {
        mExit = false;
        val item = mItems.find {
            it.shortcut == command
        }
        item?.let {
            it.command()
        }

        return !mExit;
    }
}

class MacroCommandFP(
    private val shortcut: String = "",
    private val description: String = ""
) {
    private var mCommands = listOf<Command>()

    fun execute() {
        mCommands.forEach { cmd ->
            cmd()
        }
    }

    fun addCommand(cmd: Command) {
        mCommands = mCommands.plus(cmd)
    }

    fun getName() = shortcut
    fun getDescription() = description
}