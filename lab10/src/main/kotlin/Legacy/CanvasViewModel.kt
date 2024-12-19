/*
package Legacy

import Models.ModelShape
import Models.Models
import View.Primitive
import ViewModel.Figures.Ellipse
import ViewModel.Figures.Rectangle
import ViewModel.Figures.Triangle
import ViewModel.Styles.FillStyle
import ViewModel.Styles.StrokeStyle
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

interface IViewModel {
    fun addRectangle()
    fun addEllipse()
    fun addTriangle()
    fun addImage()
    fun selectPrimitive(primitive: Primitive)
    fun movePrimitive(deltaX: Float, deltaY: Float)
    fun undo()
    fun redo()
}

data class CanvasState(
    val shapes: List<Shape> = emptyList(),
)

class CanvasViewModel(
    private val dataModel: Models,
    private val windowWidth: Int,
    private val windowHeight: Int,
    private val defaultWidth: Int,
    private val defaultHeight: Int
): IViewModel, ViewModel()  {
    private val mSelectedShape = mutableStateOf<Shape?>(null)
    private val mCanvasState = mutableStateOf(CanvasState())

    val state: State<CanvasState> = mCanvasState
    private val viewModelScope = CoroutineScope(Job() + Dispatchers.IO)

    init {
        viewModelScope.launch {
            delay(500)
            dataModel.mShapes.onEach {
                mCanvasState.value = map(it)
            }.launchIn(viewModelScope)
        }
    }

    override fun movePrimitive(deltaX: Float, deltaY: Float) {
        mSelectedShape.value?.let {
            val shape = it
            val newX = it.getFrame().left + deltaX.toInt()
            val newY = it.getFrame().top + deltaY.toInt()
            mSelectedShape.value = shape.copy(
                newX.toFloat(), newY.toFloat() // fixme привести всё к одному виду
            )
        }
    }

    fun selectPrimitive(x: Float, y: Float) {
        mCanvasState.value.shapes.findLast {
            it.getFrame().left < x && x < it.getFrame().left +  it.getFrame().width &&
                    it.getFrame().top < y && y <  it.getFrame().top + it.getFrame().height
        }?.let {
            var newShapes = mCanvasState.value.shapes.minus(it)
            mSelectedShape.value?.let { shape ->
                newShapes = newShapes.plus(shape)
            }
            mSelectedShape.value = it
            mCanvasState.value = mCanvasState.value.copy(
                shapes = newShapes
            )
        } ?: {
            mSelectedShape.value?.let {
                mCanvasState.value = mCanvasState.value.copy(
                    shapes = mCanvasState.value.shapes.plus(it)
                )
                mSelectedShape.value = null
            }
        }
    }

    fun unslectPrimitive() {
        mSelectedShape.value?.let {
            mCanvasState.value = mCanvasState.value.copy(
                shapes = mCanvasState.value.shapes.plus(it)
            )
            mSelectedShape.value = null
        }
    }

    fun deleteSelectedShape() {
    }

    override fun addRectangle() {
        dataModel.addShape(ModelShape.Rectangle(
            UUID.randomUUID().toString(),
            (windowWidth / 2).dp.value,
            (windowHeight / 2).dp.value,
            defaultWidth.toFloat(),
            defaultHeight.toFloat(),
            Color.Black
        ))
    }
    //FIXME MOCK
    override fun addEllipse() {
        dataModel.addShape(ModelShape.Rectangle(
            UUID.randomUUID().toString(),
            (windowWidth / 2).dp.value,
            (windowHeight / 2).dp.value,
            defaultWidth.toFloat(),
            defaultHeight.toFloat(),
            Color.Red
        ))
    }
    //FIXME MOCK
    override fun addTriangle() {
        dataModel.addShape(ModelShape.Rectangle(
            UUID.randomUUID().toString(),
            (windowWidth / 2).dp.value,
            (windowHeight / 2).dp.value,
            defaultWidth.toFloat(),
            defaultHeight.toFloat(),
            Color.Green
        ))
    }

    override fun addImage() {
        TODO("Not yet implemented")
    }

    override fun selectPrimitive(primitive: Primitive) {
        TODO("Not yet implemented")
    }

    override fun undo() {
        TODO("Not yet implemented")
    }

    override fun redo() {
        TODO("Not yet implemented")
    }

    private fun map(shapes: List<ModelShape>?): CanvasState {
        return mCanvasState.value.copy(
            shapes = shapes?.map { it.toShape() } ?: mCanvasState.value.shapes,
        )
    }
}


//FIXME ДОБАВИТЬ ДРУГИЕ ФИГУРЫ
fun ModelShape.toShape() = when(this) {
    is ModelShape.Rectangle -> Rectangle(
        RectI(
            this.x.value.toInt(),
            this.y.value.toInt(),
            this.width.value.toInt(),
            this.height.value.toInt()
        ),
        StrokeStyle(
            1u,
            color.value.value// fixme mock добавить цвет обводки
        ),
        FillStyle(
            color.value.value
        ) // fixme mock добавить цвет обводки
    )
    is ModelShape.Ellipse -> Rectangle(
        RectI(
            this.x.value.toInt(),
            this.y.value.toInt(),
            this.width.value.toInt(),
            this.height.value.toInt()
        ),
        StrokeStyle(
            1u,
            color.value.value// fixme mock добавить цвет обводки
        ),
        FillStyle(
            color.value.value
        ) // fixme mock добавить цвет обводки
    )
    is ModelShape.Triangle -> Rectangle(
        RectI(
            this.x.value.toInt(),
            this.y.value.toInt(),
            this.width.value.toInt(),
            this.height.value.toInt()
        ),
        StrokeStyle(
            1u,
            color.value.value// fixme mock добавить цвет обводки
        ),
        FillStyle(
            color.value.value
        ) // fixme mock добавить цвет обводки
    )
}

fun Shape.copy(
    x: Float, y: Float,
    width: Float? = null,
    height: Float? = null,
    color: Color? = null
): Shape {
    return when (this) {
        is Rectangle -> Rectangle(
            RectI(
                this.getFrame().left,
                this.getFrame().top,
                this.getFrame().width,
                this.getFrame().height
            ),
            StrokeStyle(
                1u,
                color?.value// fixme mock добавить цвет обводки
            ),
            FillStyle(
                color?.value
            ) // fixme mock добавить цвет обводки
        )

        is Ellipse ->  Rectangle(
            RectI(
                this.getFrame().left,
                this.getFrame().top,
                this.getFrame().width,
                this.getFrame().height
            ),
            StrokeStyle(
                1u,
                color?.value// fixme mock добавить цвет обводки
            ),
            FillStyle(
                color?.value
            ) // fixme mock добавить цвет обводки
        )

        is Triangle ->  Rectangle(
            RectI(
                this.getFrame().left,
                this.getFrame().top,
                this.getFrame().width,
                this.getFrame().height
            ),
            StrokeStyle(
                1u,
                color?.value// fixme mock добавить цвет обводки
            ),
            FillStyle(
                color?.value
            ) // fixme mock добавить цвет обводки
        )

        else -> {
            throw IllegalArgumentException("Unknow type figure")
        }
    }
}


*/
/*
fun Shape.copy(
): Shape {
    return when (this) {
        is Shape.Rectangle -> Shape.Rectangle(
            id ?: this.id,
            x = this.x.value,
            y = this.y.value,
            width = this.width.value,
            height = this.height.value
        )

        is Shape.Ellipse -> Shape.Ellipse(
            x = this.x,
            y = this.y,
            width = this.width,
            height = this.height
        )

        is Shape.Triangle -> Shape.Triangle(id ?: this.id, x = this.x, y = this.y, size)
    }
}
*/

