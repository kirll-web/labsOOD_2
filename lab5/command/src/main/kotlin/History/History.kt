package org.command.History

import org.command.Command.*
import java.io.File

class History : ICommandExecutor {
    private var mCommands = mutableListOf<ICommand>()
    private var mNextCommandIndex = 0

    fun canUndo() = mNextCommandIndex != 0

    fun undo() {
        if (canUndo()) {
            mCommands[mNextCommandIndex - 1].unexecute()
            --mNextCommandIndex
        }
    }

    fun redo() {
        if (canRedo()) {
            mCommands[mNextCommandIndex].execute() // может выбросить исключение
            ++mNextCommandIndex
        }
    }

    fun canRedo() = mNextCommandIndex != mCommands.size

    override fun addAndExecuteCommand(command: ICommand) {
        when {
            mNextCommandIndex < mCommands.size -> {
                command.execute()
                val removeCommands = mCommands.slice(mNextCommandIndex  until mCommands.size)
                removeCommands.forEach {
                    it.removeCommand()
                }
                mCommands = mCommands.slice(0 until  mNextCommandIndex).toMutableList()
                ++mNextCommandIndex
                mCommands.addLast(command)
            }

            else -> {
                try {
                    command.execute()
                    if(mCommands.size == MAX_COMMANDS_SIZE) {
                        val command = mCommands.removeFirst()
                        command.removeCommand()
                        --mNextCommandIndex
                    }

                    //переписать условие большее понятно
                    if (!(mCommands.isNotEmpty() && mCommands.last().tryMergeWith(command))) {
                        mCommands.addLast(command)
                        ++mNextCommandIndex
                    }

                } catch (ex: Exception) {
                    println(ex.message)
                }
            }
        }
    }

    private fun itemsBelongSameClass(firstCommand: ICommand, secondCommand: ICommand): Boolean {
        val slakeClasses = listOf(
            ResizeImageCommand::class.java.name,
            ReplaceTextCommand::class.java.name,
            SetTitleCommand::class.java.name
        )
        val firstCommandName = firstCommand::class.java.name
        val secondCommandName = secondCommand::class.java.name

        return when {
            firstCommandName in slakeClasses
                    && secondCommandName in slakeClasses
                    && firstCommandName == secondCommandName -> true
            else -> false
        }
    }

    companion object {
        private const val MAX_COMMANDS_SIZE = 10
    }
}