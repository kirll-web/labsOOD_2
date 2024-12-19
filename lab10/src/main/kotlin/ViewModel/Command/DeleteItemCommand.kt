package Command

import Models.IModels
import Models.ModelShape
import Models.Models

class DeleteItemCommand(
    private var dataModel: IModels,
    private val value: ModelShape,
    private var position: Int? = null
) : AbstractCommand() {
    override fun doExecute() {
        dataModel.removeShapeById(value.id)
    }
    override fun doUnexecute() {
        dataModel.addShape(value, position)
    }
}

