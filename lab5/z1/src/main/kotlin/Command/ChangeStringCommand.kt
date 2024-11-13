package Command


class ChangeStringCommand (
    private var mTarget: String,
    private var mNewValue: String
): AbstractCommand() {
    override fun doExecute() {
        val temp = mNewValue
        mNewValue = mTarget
        mTarget = temp
    }
    override fun doUnexecute() {
        val temp = mNewValue
        mNewValue = mTarget
        mTarget = temp
    }
}