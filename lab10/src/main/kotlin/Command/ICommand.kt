package Command

interface ICommand {
    val isExecuted: Boolean
    fun execute()
    fun unexecute()
    fun removeCommand() {}

    fun tryMergeWith(other: ICommand) = false
}