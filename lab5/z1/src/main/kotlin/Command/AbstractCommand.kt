package Command

abstract class AbstractCommand: ICommand {
    private var mExecuted = false
    override fun execute() {
        DoExecute()
        mExecuted = true
    }
    override fun unexecute() {
        DoExecute()
        mExecuted = true
    }

    fun DoExecute() = 0;
    fun DoUnexecute() = 0;
}