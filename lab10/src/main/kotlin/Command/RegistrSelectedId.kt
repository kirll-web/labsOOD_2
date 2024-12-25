package Command

import androidx.compose.runtime.MutableState

class RegistrSelectedId(
    private var stateId: MutableState<String?>,
    val value: String?,
) : AbstractCommand() {
    private val mValue = value
    private var mOldValue: String? = null
    override fun doExecute() {
        mOldValue = stateId.value
        stateId.value = mValue
    }

    override fun doUnexecute() {
        stateId.value = mOldValue
    }

    override fun tryMergeWith(other: ICommand): Boolean {
        return if (other is RegistrSelectedId && value == other.value) true
        else false
    }
}
