package Models

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class Models {
    private val mShapes = MutableStateFlow(mapOf<String, ModelShape>())
    val shapes: StateFlow<Map<String, ModelShape>> = mShapes

    fun addShape(shape: ModelShape) {
        mShapes.value = mShapes.value.toMap().plus(shape.id to shape)
    }

    fun updateShape(shape: ModelShape) {
        mShapes.value = mShapes.value.toMutableMap().apply {
            this[shape.id] = shape
        }
    }

    fun removeShapeById(id: String) {
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

