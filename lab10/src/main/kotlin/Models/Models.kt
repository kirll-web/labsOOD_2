package Models

import Command.AddShapeCommand
import Command.DeleteItemCommand
import Command.MoveShapeCommand
import History.History
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


//переименовать
interface IModels {
    val shapes: StateFlow<Map<String, ModelShape>>
    fun addShape(shape: ModelShape, position: Int? = null)
    fun updateShape(shape: ModelShape)
    fun removeShapeById(id: String)
    fun undo()
    fun redo()
}

class Models: IModels {
    private val mShapes = MutableStateFlow(mapOf<String, ModelShape>())
    private val mHistory = History()

    override val shapes: StateFlow<Map<String, ModelShape>> = mShapes

    override fun addShape(shape: ModelShape, position: Int?) {
        mHistory.addAndExecuteCommand(
            AddShapeCommand(mShapes, shape, position)
        )
    }

    //replayShape переименовать
    override fun updateShape(shape: ModelShape) {
        val oldShape = mShapes.value[shape.id]

        oldShape?.let {
            mHistory.addAndExecuteCommand(
                MoveShapeCommand(
                    {
                        mShapes.value = mShapes.value.toMutableMap().apply {
                            this[shape.id] = shape
                        }
                    },
                    {
                        mShapes.value = mShapes.value.toMutableMap().apply {
                            this[shape.id] = oldShape
                        }
                    }
                )
            )
        }
    }

    override fun removeShapeById(id: String) {
        mShapes.value[id]?.let { shape ->
            mHistory.addAndExecuteCommand(
                //FIXME если выделенная фигура была удалена, то она должна стать выделенной при undo
                DeleteItemCommand(mShapes, shape, mShapes.value.keys.indexOf(shape.id))
            )
        }
    }

    override fun undo() {
        if (mHistory.canUndo()) {
            mHistory.undo()
        }
    }

    override fun redo() {
        if (mHistory.canRedo()) {
            mHistory.redo()
        }
    }
}

sealed class ModelShape(
    open val id: String,
    open val x: Float = 0f,
    open val y: Float = 0f,
    open val width: Float = 0f,
    open val height: Float = 0f,
    open val color: ULong = 0x000000uL
) {
    class Rectangle(
        override val id: String,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: ULong
    ) : ModelShape(
        id,
        x, y, width, height, color
    )

    class Ellipse(
        override val id: String,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: ULong
    ) : ModelShape(
        id,
        x, y, width, height, color
    )

    class Triangle(
        override val id: String, x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: ULong
    ) : ModelShape(
        id,
        x, y, width, height, color
    )
}

