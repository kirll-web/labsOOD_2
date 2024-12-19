package Command

import Models.ModelShape
import Models.Models

class DeleteItemCommand(
    private var dataModel: Models,
    private val value: ModelShape,
    private var position: Int? = null
) : AbstractCommand() {
    override fun doExecute() {
        dataModel.removeShapeById(value.id)
    }
    override fun doUnexecute() {
        /*position?.let {
            val newList = items.toMutableList()
            newList[it] = value
            items = newList.toList()
        }*/ //fixme mock
        dataModel.addShape(value)
    }
}

