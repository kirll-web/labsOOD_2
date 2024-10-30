package Command

interface ICommandExecutor {
    fun AddAndExecuteCommand(command: ICommand)
}