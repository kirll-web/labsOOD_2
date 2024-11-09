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
            mNextCommandIndex < mCommands.size -> {// Не происходит расширения истории команд
                command.execute()
                ++mNextCommandIndex
                mCommands.addLast(command)
            }

            else -> {// будет происходить расширение истории команд
                if (mNextCommandIndex == mCommands.size) {
                    return
                }

                command.execute() // может выбросить исключение
                try {
                    command.execute()
                    // заменяем команду-заглушку
                    mCommands.addLast(command) // не бросает исключений
                    ++mNextCommandIndex // теперь можно обновить индекс следующей команды
                } catch (ex: Exception) {
                    //todo
                }
            }
        }

    }
}