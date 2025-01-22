package History

import Command.ICommand
import Command.ICommandExecutor

class History: ICommandExecutor {
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
                val commands = mCommands.slice(0 until  mNextCommandIndex).toMutableList()
                commands.addLast(command)
                mCommands = commands
                ++mNextCommandIndex
            }

            else -> {
                try {
                    command.execute()
                    if(mCommands.size == MAX_COMMANDS_SIZE) {
                        mCommands.removeFirst().removeCommand()
                        --mNextCommandIndex
                    }

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


    companion object {
        private const val MAX_COMMANDS_SIZE = 30
    }
}