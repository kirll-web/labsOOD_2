package org.command.Command

interface ICommand {
    val isExecuted: Boolean
    fun execute()
    fun unexecute()
}