package Command

interface ICommandExecutor {
    fun addAndExecuteCommand(command: ICommand)
}