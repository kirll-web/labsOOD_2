package Command

import Models.ModelShape
import Models.Models
import kotlinx.coroutines.flow.MutableStateFlow

class AddShapeCommand(
    private var dataModel: Models, //fixme пофиксить названия дата слоя
    value: ModelShape,
) : AbstractCommand() {
    private val mValue = value
    val value: ModelShape
        get() = mValue

    private var mOldValue: ModelShape? = null
    override fun doExecute() {
        dataModel.addShape(value)
    }

    override fun doUnexecute() {
        dataModel.removeShapeById(value.id)
    }

    override fun removeCommand() {
        //mValue.remove()
    }
}
