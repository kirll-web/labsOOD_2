package Command

abstract class AbstractCommand: ICommand {
    private var mExecuted = false
    override fun execute() {
        doExecute()
        mExecuted = true
    }
    override fun unexecute() {
        doExecute()
        mExecuted = true
    }

    abstract fun doExecute()
    abstract fun doUnexecute()
}