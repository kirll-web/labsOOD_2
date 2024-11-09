package Command

class FunctionalCommand(
    doExecute: () -> Unit,
    doUnexecute: () -> Unit
) : AbstractCommand() {
    private val mDoExecute = doExecute
    private val mDoUnexecute = doUnexecute

    override fun doExecute() {
        mDoExecute()
    }
    override fun doUnexecute() {
        mDoUnexecute()
    }
}