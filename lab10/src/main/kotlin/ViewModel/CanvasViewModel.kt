package ViewModel

import Command.AddShapeCommand
import Command.DeleteItemCommand
import Command.MoveShapeCommand
import Command.ResizeShapeCommand
import History.History
import Models.ModelShape
import Models.Models
import ViewModel.Figures.Ellipse
import ViewModel.Figures.Rectangle
import ViewModel.Figures.Triangle
import ViewModel.Figures.TrianglePoints
import ViewModel.Styles.FillStyle
import ViewModel.Styles.StrokeStyle
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

interface ICanvasViewModel {
    val state: State<CanvasState>
    fun addRectangle()
    fun addEllipse()
    fun addTriangle()
    fun addImage()
    fun onDragStart(x: Float, y: Float)
    fun onDrag(x: Float, y: Float)
    fun onDragEnd()
    fun selectShape(x: Float, y: Float)
    // Зачем этот метод публичный, если он вызывается только из вьюмодели
    // Зачем тут передается айдишник, если выделяется всегда 1 шейп
    fun unselectShape(shapeId: String?)
    fun undo()
    fun redo()
    fun tryDeleteSelectedShape(): Boolean
    fun changeWindowSize(
        width: Float,
        height: Float
    )
}

data class CanvasState(
    val shapes: Map<String, IShape> = emptyMap(),
)

//переименовать можно на compose canvas
class CanvasViewModel(
    private val dataModel: Models,
    private var windowWidth: Int,
    private var windowHeight: Int,
    private val defaultWidth: Int,
    private val defaultHeight: Int
) : ICanvasViewModel, ViewModel() {
    private val mSelectedShapeId = mutableStateOf<String?>(null)
    private val mCanvasState = mutableStateOf(CanvasState())
    private val viewModelScope = CoroutineScope(Job() + Dispatchers.IO)
    private var mDragEvent: OnDragEvent? = null
    private val mHistory = History()

    enum class OnDragEvent {
        MOVE,
        RESIZE
    }

    override val state: State<CanvasState> = mCanvasState

    init {
        viewModelScope.launch {
            delay(500)
            dataModel.shapes.onEach {
                mCanvasState.value = map(it)
            }.launchIn(viewModelScope)
        }
    }

    override fun selectShape(x: Float, y: Float) {
        val selectedShape = mCanvasState.value.shapes.values.findLast {
            it.getFrame().left < x && x < it.getFrame().left + it.getFrame().width &&
                    it.getFrame().top < y && y < it.getFrame().top + it.getFrame().height
        }

        when (selectedShape?.id) {
            null -> unselectShape(mSelectedShapeId.value)

            mSelectedShapeId.value -> return

            else -> {
                val oldSelectShapeId = mSelectedShapeId.value
                unselectShape(oldSelectShapeId)
                mSelectedShapeId.value = selectedShape.id
                dataModel.removeShapeById(selectedShape.id)
                dataModel.addShape(selectedShape.toModelShape())
            }
        }
    }

    override fun onDragStart(x: Float, y: Float) {
        mSelectedShapeId.value?.let { id ->
            //мудрённая логика
            val startedDragShape = mCanvasState.value.shapes[id]
            startedDragShape?.let {
                mDragEvent = when {
                    (startedDragShape.getFrame().left < x &&
                            x < startedDragShape.getFrame().left + startedDragShape.getFrame().width &&
                            startedDragShape.getFrame().top < y &&
                            y < startedDragShape.getFrame().top + startedDragShape.getFrame().height) -> OnDragEvent.MOVE

                    (x > startedDragShape.getFrame().left + startedDragShape.getFrame().width &&
                            x < startedDragShape.getFrame().left + startedDragShape.getFrame().width + Shape.SIZE_FRAME_POINTS &&
                            y > startedDragShape.getFrame().top + startedDragShape.getFrame().height &&
                            y < startedDragShape.getFrame().top + startedDragShape.getFrame().height + Shape.SIZE_FRAME_POINTS) -> OnDragEvent.RESIZE

                    else -> {
                        unselectShape(startedDragShape.id)
                        null
                    }
                }
            }
        }

    }

    override fun onDrag(x: Float, y: Float) {
        when (mDragEvent) {
            OnDragEvent.RESIZE -> resizeShape(x, y)
            OnDragEvent.MOVE -> moveShape(x, y)
            else -> Unit
        }
    }

    override fun onDragEnd() {
        mDragEvent = null
    }
    //fixme нет смысла передавать id, т.к. он всегда один и тотже
    override fun unselectShape(shapeId: String?) {
        mSelectedShapeId.value = null
        shapeId?.let { id ->
            val shape = mCanvasState.value.shapes[id]
            shape?.let {
                mCanvasState.value.shapes
                    .minus(shape.id)
                    .plus(shapeId to shape.copy(isSelect = false))
                    .values.forEach { sh ->
                        dataModel.removeShapeById(sh.id)
                        dataModel.addShape(sh.toModelShape())
                    }
            }
        }
    }

    override fun tryDeleteSelectedShape(): Boolean {
        val id = mSelectedShapeId.value
        return when {
            id != null -> {
                mSelectedShapeId.value = null
                removeShapeById(id)
                true
            }

            else -> false
        }
    }

    override fun changeWindowSize(width: Float, height: Float) {
        windowWidth = width.toInt()
        windowHeight = height.toInt()
    }

    override fun addRectangle() {
        unselectShape(mSelectedShapeId.value)
        addShape(
            //fixme вынести в фабрику
            ModelShape.Rectangle(
                UUID.randomUUID().toString(),
                (windowWidth / 2 - defaultWidth / 2).toFloat(),
                (windowHeight / 2 - defaultHeight / 2).toFloat(),
                defaultWidth.toFloat(),
                defaultHeight.toFloat(),
                Color.Black
            )
        )
    }

    override fun addEllipse() {
        unselectShape(mSelectedShapeId.value)
        addShape(
            ModelShape.Ellipse(
                UUID.randomUUID().toString(),
                (windowWidth / 2 - defaultWidth / 2).toFloat(),
                (windowHeight / 2 - defaultHeight / 2).toFloat(),
                defaultWidth.toFloat(),
                defaultHeight.toFloat(),
                Color.Red
            )
        )
    }

    override fun addTriangle() {
        unselectShape(mSelectedShapeId.value)
        addShape(
            ModelShape.Triangle(
                UUID.randomUUID().toString(),
                (windowWidth / 2 - defaultWidth / 2).toFloat(),
                (windowHeight / 2 - defaultHeight / 2).toFloat(),
                defaultWidth.toFloat(),
                defaultHeight.toFloat(),
                Color.Green
            )
        )
    }

    private fun addShape(modelShape: ModelShape) {
        mHistory.addAndExecuteCommand(
            AddShapeCommand(dataModel, modelShape)
        )
    }

    private fun removeShapeById(id: String) {
        mCanvasState.value.shapes[id]?.let { shape ->
            mHistory.addAndExecuteCommand(
                DeleteItemCommand(dataModel, shape.toModelShape())
            )
        }
    }

    override fun addImage() {
        TODO("Not yet implemented")
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

    private fun map(shapes: List<ModelShape>?): CanvasState {
        return mCanvasState.value.copy(
            shapes = shapes?.map {
                val isSelect = it.id == mSelectedShapeId.value
                it.id to it.toShape(isSelect)
            }?.toMap() ?: mCanvasState.value.shapes
        )
    }

    private fun moveShape(deltaX: Float, deltaY: Float) {
        mSelectedShapeId.value?.let { id ->
            mCanvasState.value.shapes[id]?.let { shape ->
                val newShape = shape.copy()
                newShape.setFrame(
                    RectI(
                        shape.getFrame().left + deltaX.toInt(),
                        shape.getFrame().top + deltaY.toInt(),
                        0,
                       0
                    )
                )

                val newFrame = newShape.getFrame()
                if (newFrame.left + newFrame.width > windowWidth ||
                    newFrame.left < 0 ||
                    newFrame.top < 0 ||
                    newFrame.top + newFrame.height > windowHeight
                ) return

                mHistory.addAndExecuteCommand(
                    MoveShapeCommand(
                        {
                            dataModel.removeShapeById(id)
                            dataModel.addShape(newShape.toModelShape())
                        },
                        {
                            dataModel.removeShapeById(id)
                            dataModel.addShape(shape.toModelShape())
                        }
                    )
                )
            }
        }
    }

    private fun resizeShape(deltaX: Float, deltaY: Float) {
        println("resize 1")
        mSelectedShapeId.value?.let { id ->
            mCanvasState.value.shapes[id]?.let { shape ->
                val newShape = shape.copy()
                newShape.setFrame(
                    RectI(
                        shape.getFrame().left,
                        shape.getFrame().top,
                        shape.getFrame().width * deltaX.toInt(),
                        shape.getFrame().height * deltaY.toInt()
                    )
                )

                if (newShape.getFrame().height <= 0 || newShape.getFrame().width <= 0) return
                if (
                    newShape.getFrame().height + newShape.getFrame().top > windowHeight
                    || newShape.getFrame().width + shape.getFrame().left
                    > windowWidth
                ) return
                println(" ${shape.getFrame().width} ${newShape.getFrame().width} ")
                mHistory.addAndExecuteCommand(
                    ResizeShapeCommand(
                        {
                            dataModel.removeShapeById(id)
                            dataModel.addShape(newShape.toModelShape())
                        },
                        {
                            dataModel.removeShapeById(id)
                            dataModel.addShape(shape.toModelShape())
                        }
                    )
                )
            }
        }
    }
}

fun ModelShape.toShape(
    isSelect: Boolean? = null
) = when (this) {
    is ModelShape.Rectangle -> Rectangle(
        this.id,
        RectI(
            x.value.toInt(),
            y.value.toInt(),
            width.value.toInt(),
            height.value.toInt()
        ),
        StrokeStyle(
            0u,
            null
        ),
        FillStyle(
            color.value.value
        ),
        isSelect ?: false
    )

    is ModelShape.Ellipse -> Ellipse(
        this.id,
        RectI(
            x.value.toInt(),
            y.value.toInt(),
            width.value.toInt(),
            height.value.toInt()
        ),
        StrokeStyle(
            0u,
            null
        ),
        FillStyle(
            color.value.value
        ),
        isSelect ?: false
    )

    is ModelShape.Triangle -> Triangle(
        this.id,
        TrianglePoints(
            leftBottom = Point(
                x.value.toInt(), y.value.toInt() + height.value.toInt()
            ),
            top = Point(
                x.value.toInt() + width.value.toInt() / 2, y.value.toInt()
            ),
            rightBottom = Point(
                x.value.toInt() + width.value.toInt(),
                y.value.toInt() + height.value.toInt()
            )
        ),
        StrokeStyle(
            0u,
            null
        ),
        FillStyle(
            color.value.value
        ),
        isSelect ?: false
    )
}

fun IShape.toModelShape() = when (this) {
    is Rectangle -> ModelShape.Rectangle(
        this.id,
        this.getFrame().left.toFloat(),
        this.getFrame().top.toFloat(),
        this.getFrame().width.toFloat(),
        this.getFrame().height.toFloat(),
        getFillStyle().getColor()?.let { Color(it) } ?: Color.Black
    )

    is Ellipse -> ModelShape.Ellipse(
        this.id,
        this.getFrame().left.toFloat(),
        this.getFrame().top.toFloat(),
        this.getFrame().width.toFloat(),
        this.getFrame().height.toFloat(),
        getFillStyle().getColor()?.let { Color(it) } ?: Color.Black
    )

    is Triangle -> ModelShape.Triangle(
        this.id,
        this.getFrame().left.toFloat(),
        this.getFrame().top.toFloat(),
        this.getFrame().width.toFloat(),
        this.getFrame().height.toFloat(),
        getFillStyle().getColor()?.let { Color(it) } ?: Color.Black
    )

    else -> throw IllegalArgumentException("Unknowed figure")
}

fun IShape.copy(
    x: Int? = null, y: Int? = null,
    width: Int? = null,
    height: Int? = null,
    color: Color? = null,
    isSelect: Boolean? = null,
): IShape {
    return when (this) {
        is Rectangle -> Rectangle(
            id,
            RectI(
                x ?: getFrame().left,
                y ?: getFrame().top,
                width ?: getFrame().width,
                height ?: getFrame().height
            ),
            getStrokeStyle(),
            when (color) {
                null -> getFillStyle()
                else -> FillStyle(color.value)
            },
            isSelect ?: this.isSelect
        )

        is Ellipse -> Ellipse(
            this.id,
            RectI(
                x ?: getFrame().left,
                y ?: getFrame().top,
                width ?: getFrame().width,
                height ?: getFrame().height
            ),
            getStrokeStyle(),
            when (color) {
                null -> getFillStyle()
                else -> FillStyle(color.value)
            },
            isSelect ?: this.isSelect
        )

        is Triangle -> Triangle(
            this.id,
            TrianglePoints(
                leftBottom = Point(
                    x ?: getFrame().left,
                    (y ?: getFrame().top) + (height ?: getFrame().height)
                ),
                top = Point(
                    (x ?: getFrame().left) + (width ?: getFrame().height) / 2,
                    y ?: getFrame().top
                ),
                rightBottom = Point(
                    (x ?: getFrame().left) + (width ?: getFrame().width),
                    (y ?: getFrame().top) + (height ?: getFrame().height)
                )
            ),
            getStrokeStyle(),
            when (color) {
                null -> getFillStyle()
                else -> FillStyle(color.value)
            },
            isSelect ?: this.isSelect
        )

        else -> {
            throw IllegalArgumentException("Unknow type figure")
        }
    }

}
