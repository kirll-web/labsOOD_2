package org.command.Command

abstract class AbstractCommand: ICommand {
    private var mIsExecuted = false
    override val isExecuted
        get() = mIsExecuted


    override fun execute() {
        doExecute()
        mIsExecuted = true
    }

    override fun unexecute() {
        doUnexecute()
        mIsExecuted = false
    }

    abstract fun doExecute()
    abstract fun doUnexecute()
}