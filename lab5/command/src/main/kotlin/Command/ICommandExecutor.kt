package org.command.Command

interface ICommandExecutor {
    fun addAndExecuteCommand(command: ICommand)
}