package Command

abstract class FunctionalCommand(
    doExecute: () -> Unit,
    doUnexecute: () -> Unit
): AbstractCommand() {
    private var mDoExecute = doExecute
    private var mDoUnexecute = doUnexecute

    override fun doExecute() {
        mDoExecute()
    }

    override fun doUnexecute() {
        mDoUnexecute()
    }

    override fun tryMergeWith(other: ICommand) = when {
        other.javaClass.name == this.javaClass.name ->  {
            mDoExecute = {
                other.execute()
            }
            true
        }
        else -> false
    }
}