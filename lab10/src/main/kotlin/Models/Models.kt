package Models

import Command.AddShapeCommand
import Command.DeleteItemCommand
import History.History
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class Models {
    private val mShapes = MutableStateFlow(emptyList<ModelShape>().toMutableList())
    private val mSelectedShape: MutableStateFlow<ModelShape?> = MutableStateFlow(null)

    val shapes: StateFlow<List<ModelShape>> = mShapes

    fun addShape(shape: ModelShape) {
        mShapes.value = mShapes.value.toMutableList().apply {
            add(shape)
        }
    }

    fun removeShapeById(id: String) {
        mShapes.value.find { it.id == id }?.let {
            mShapes.value = mShapes.value.toMutableList().apply {
                remove(it)
            }
        }
    }
}

sealed class ModelShape(
    open val id: String,
    open val x: MutableState<Float> = mutableStateOf(0f),
    open val y: MutableState<Float> = mutableStateOf(0f),
    open val width: MutableState<Float> = mutableStateOf(0f),
    open val height: MutableState<Float> = mutableStateOf(0f),
    open val color: MutableState<Color> = mutableStateOf(Color.Black) // fixme mock
) {
    abstract fun move(deltaX: Float, deltaY: Float)
    abstract fun resize(width: Float, height: Float)

    class Rectangle(
        override val id: String,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Color
    ) : ModelShape(
        id,
        mutableStateOf(x),
        mutableStateOf(y),
        mutableStateOf(width),
        mutableStateOf(height),
        mutableStateOf(color)
    ) {
        override fun move(deltaX: Float, deltaY: Float) {
            TODO("Not yet implemented")
        }

        override fun resize(deltaX: Float, deltaY: Float) {
            TODO("Not yet implemented")
        }
    }

    class Ellipse(
        override val id: String,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Color
    ) : ModelShape(
        id,
        mutableStateOf(x),
        mutableStateOf(y),
        mutableStateOf(width),
        mutableStateOf(height),
        mutableStateOf(color)
    ) {
        override fun move(deltaX: Float, deltaY: Float) {
            TODO("Not yet implemented")
        }

        override fun resize(deltaX: Float, deltaY: Float) {
            TODO("Not yet implemented")
        }
    }

    class Triangle(
        override val id: String, x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Color
    ) : ModelShape(
        id,
        mutableStateOf(x),
        mutableStateOf(y),
        mutableStateOf(width),
        mutableStateOf(height),
        mutableStateOf(color)
    ) {
        override fun move(deltaX: Float, deltaY: Float) {
            TODO("Not yet implemented")
        }

        override fun resize(deltaX: Float, deltaY: Float) {
            TODO("Not yet implemented")
        }
    }

    fun copy(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
    ) {
        return
    }
}

