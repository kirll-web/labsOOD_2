package Command

import Models.IModels
import Models.ModelShape

class AddShapeCommand(
    private var dataModel: IModels, //fixme пофиксить названия дата слоя
    private val value: ModelShape,
) : AbstractCommand() {

    override fun doExecute() {
        dataModel.addShape(value)
    }

    override fun doUnexecute() {        dataModel.removeShapeById(value.id)
    }

    override fun removeCommand() {
        //mValue.remove()
    }
}
