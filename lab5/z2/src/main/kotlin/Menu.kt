package org.command


class Menu {
    private var mMacrosToMacro = listOf<MacroCommand>()
    private var mCurrentMacro: MacroCommand? = null
    private var mMacros = listOf<MacroCommand>()
    private var mItems = listOf<Item>()
    private var mExit = false

    data class Item(
        val shortcut: String,
        val description: String,
        val command: ICommand
    )

    fun addItem(
        shortcut: String,
        description: String,
        command: ICommand
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
        showInstructions()

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

    fun exit() {
        mExit = true
    }

    fun isRecordMacro() = mCurrentMacro != null

    fun setCurrentMacro(currentMacros: MacroCommand) {
        when {
            mItems.find { it.shortcut ==  currentMacros.getName()} == null -> mCurrentMacro = currentMacros
            else -> throw IllegalArgumentException("This macro is already exists")
        }
    }

    fun addCommandToCurrentMacro(command: ICommand) {
        mCurrentMacro?.addCommand(command)
    }

    fun addCurrentMacroMenuItem()
    {
        if (isRecordMacro())
        {
            val copyMacro = mCurrentMacro

            copyMacro?.let {
                copy -> addItem(copy.getName(), copy.getDescription(), copy)
                mMacros = mMacros.plus(copyMacro)
            }

            mCurrentMacro = null
        }
    }

    private fun executeCommand(command: String): Boolean {
        mExit = false
        val item = mItems.find {
            it.shortcut == command
        }
        item?.command?.execute()

        return !mExit
    }

}

