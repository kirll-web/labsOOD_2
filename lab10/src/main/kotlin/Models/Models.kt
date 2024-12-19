package Models

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


interface IModels {
    val shapes: StateFlow<Map<String, ModelShape>>
    fun addShape(shape: ModelShape, position: Int? = null)
    fun updateShape(shape: ModelShape)
    fun removeShapeById(id: String)
}

class Models: IModels {
    private val mShapes = MutableStateFlow(mapOf<String, ModelShape>())
    override val shapes: StateFlow<Map<String, ModelShape>> = mShapes

    override fun addShape(shape: ModelShape, position: Int?) {
        when {
            position == null -> {
                mShapes.value = mShapes.value.toMap().plus(shape.id to shape)
            }

            else ->  {
                val newShapes = mutableMapOf<String, ModelShape>()
                var index = 0
                mShapes.value.forEach {
                    if (position == index) {
                        newShapes[shape.id] = shape
                    }
                    newShapes[it.key] = it.value
                    index += 1
                }
                if (position > mShapes.value.values.size) {
                    newShapes[shape.id] = shape
                }

                mShapes.value = newShapes
            }
        }
    }

    override fun updateShape(shape: ModelShape) {
        mShapes.value = mShapes.value.toMutableMap().apply {
            this[shape.id] = shape
        }
    }

    override fun removeShapeById(id: String) {
        mShapes.value[id]?.let {
            mShapes.value = mShapes.value.toMap().minus(id)
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

