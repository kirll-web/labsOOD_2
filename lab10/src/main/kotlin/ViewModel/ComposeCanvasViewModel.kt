package ViewModel

import Command.AddShapeCommand
import Command.DeleteItemCommand
import Command.MoveShapeCommand
import Command.ResizeShapeCommand
import History.History
import Models.IModels
import Models.ModelShape
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

interface IMenuViewModel {
    fun addRectangle()
    fun addEllipse()
    fun addTriangle()
    fun addImage()
    fun tryDeleteSelectedShape(): Boolean
    fun undo()
    fun redo()
}

interface IComposeCanvasViewModel {
    val state: State<CanvasState>
    fun onDragStart(x: Float, y: Float)
    fun onDrag(x: Float, y: Float)
    fun onDragEnd()
    fun selectShape(x: Float, y: Float)
    fun changeWindowSize(
        width: Float,
        height: Float
    )
}

data class CanvasState(
    val shapes: Map<String, IShape> = emptyMap(),
)

class ComposeCanvasViewModel(
    private val dataModel: IModels,
    private var windowWidth: Int,
    private var windowHeight: Int,
    private val defaultWidthShape: Int,
    private val defaultHeightShape: Int
) : IComposeCanvasViewModel, IMenuViewModel, ViewModel() {
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
            it.isPick(x, y)
        }

        when (selectedShape?.id) {
            null -> unselectShape()

            mSelectedShapeId.value -> return

            else -> {
                unselectShape()
                mSelectedShapeId.value = selectedShape.id
                dataModel.updateShape(selectedShape.toModelShape())
            }
        }
    }

    override fun onDragStart(x: Float, y: Float) {
        mSelectedShapeId.value?.let { id ->
            //мудрённая логика
            mCanvasState.value.shapes[id]?.let { startedDragShape ->
                mDragEvent = when {
                    startedDragShape.isPick(x,y) -> OnDragEvent.MOVE

                    (x > startedDragShape.getFrame().left + startedDragShape.getFrame().width &&
                            x < startedDragShape.getFrame().left + startedDragShape.getFrame().width + Shape.SIZE_FRAME_POINTS &&
                            y > startedDragShape.getFrame().top + startedDragShape.getFrame().height &&
                            y < startedDragShape.getFrame().top + startedDragShape.getFrame().height + Shape.SIZE_FRAME_POINTS) -> OnDragEvent.RESIZE

                    else -> {
                        unselectShape()
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
        unselectShape()
        addShape(
            //fixme вынести в фабрику
            ModelShape.Rectangle(
                UUID.randomUUID().toString(),
                (windowWidth / 2 - defaultWidthShape / 2).toFloat(),
                (windowHeight / 2 - defaultHeightShape / 2).toFloat(),
                defaultWidthShape.toFloat(),
                defaultHeightShape.toFloat(),
                Color.Black.value
            )
        )
    }

    override fun addEllipse() {
        unselectShape()
        addShape(
            ModelShape.Ellipse(
                UUID.randomUUID().toString(),
                (windowWidth / 2 - defaultWidthShape / 2).toFloat(),
                (windowHeight / 2 - defaultHeightShape / 2).toFloat(),
                defaultWidthShape.toFloat(),
                defaultHeightShape.toFloat(),
                Color.Red.value
            )
        )
    }

    override fun addTriangle() {
        unselectShape()
        addShape(
            ModelShape.Triangle(
                UUID.randomUUID().toString(),
                (windowWidth / 2 - defaultWidthShape / 2).toFloat(),
                (windowHeight / 2 - defaultHeightShape / 2).toFloat(),
                defaultWidthShape.toFloat(),
                defaultHeightShape.toFloat(),
                Color.Green.value
            )
        )
    }


    private fun addShape(modelShape: ModelShape) {
        mHistory.addAndExecuteCommand(
            AddShapeCommand(dataModel, modelShape)
        )
    }

    private fun unselectShape() {
        val shapeId = mSelectedShapeId.value
        mSelectedShapeId.value = null
        shapeId?.let { id ->
            val shape = mCanvasState.value.shapes[id]
            shape?.let {
                mCanvasState.value.shapes
                    .minus(shape.id)
                    .plus(shapeId to shape.copy(isSelect = false))
                    .values.forEach { sh ->
                        dataModel.updateShape(sh.toModelShape())
                    }
            }
        }
    }

    private fun removeShapeById(id: String) {
        mCanvasState.value.shapes[id]?.let { shape ->
            mHistory.addAndExecuteCommand(
                DeleteItemCommand(dataModel, shape.toModelShape(), mCanvasState.value.shapes.keys.indexOf(shape.id))
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

    private fun map(shapes: Map<String, ModelShape>?): CanvasState {
        var isHasSelected = false
        val canvasState = mCanvasState.value.copy(
            shapes = shapes?.map { item ->
                val isSelect = item.key == mSelectedShapeId.value
                if (isSelect) isHasSelected = true
                item.key to item.value.toShape(isSelect)
            }?.toMap() ?: mCanvasState.value.shapes
        )
        if (!isHasSelected) mSelectedShapeId.value = null
        return canvasState
    }

    private fun moveShape(deltaX: Float, deltaY: Float) {
        mSelectedShapeId.value?.let { id ->
            mCanvasState.value.shapes[id]?.let { shape ->
                mHistory.addAndExecuteCommand(
                    MoveShapeCommand(
                        {
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
                            ) return@MoveShapeCommand
                            dataModel.updateShape(newShape.toModelShape())
                        },
                        {
                            dataModel.updateShape(shape.toModelShape())
                        }
                    )
                )
            }
        }
    }

    private fun resizeShape(deltaX: Float, deltaY: Float) {
        mSelectedShapeId.value?.let { id ->
            mCanvasState.value.shapes[id]?.let { shape ->
                mHistory.addAndExecuteCommand(
                    ResizeShapeCommand(
                        {
                            val newShape = shape.copy()
                            newShape.setFrame(
                                RectI(
                                    shape.getFrame().left,
                                    shape.getFrame().top,
                                    shape.getFrame().width * deltaX.toInt(),
                                    shape.getFrame().height * deltaY.toInt()
                                )
                            )

                            if (newShape.getFrame().height <= 0
                                || newShape.getFrame().width <= 0
                            ) return@ResizeShapeCommand
                            if (
                                newShape.getFrame().height + newShape.getFrame().top > windowHeight
                                || newShape.getFrame().width + shape.getFrame().left > windowWidth
                            ) return@ResizeShapeCommand
                            dataModel.updateShape(newShape.toModelShape())
                        },
                        {
                            dataModel.updateShape(shape.toModelShape())
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
            x.toInt(),
            y.toInt(),
            width.toInt(),
            height.toInt()
        ),
        StrokeStyle(
            0u,
            null
        ),
        FillStyle(
            color
        ),
        isSelect ?: false
    )

    is ModelShape.Ellipse -> Ellipse(
        this.id,
        RectI(
            x.toInt(),
            y.toInt(),
            width.toInt(),
            height.toInt()
        ),
        StrokeStyle(
            0u,
            null
        ),
        FillStyle(
            color
        ),
        isSelect ?: false
    )

    is ModelShape.Triangle -> Triangle(
        this.id,
        TrianglePoints(
            leftBottom = Point(
                x.toInt(), y.toInt() + height.toInt()
            ),
            top = Point(
                x.toInt() + width.toInt() / 2, y.toInt()
            ),
            rightBottom = Point(
                x.toInt() + width.toInt(),
                y.toInt() + height.toInt()
            )
        ),
        StrokeStyle(
            0u,
            null
        ),
        FillStyle(
            color
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
        getFillStyle().getColor()?.let { Color(it).value } ?: Color.Black.value
    )

    is Ellipse -> ModelShape.Ellipse(
        this.id,
        this.getFrame().left.toFloat(),
        this.getFrame().top.toFloat(),
        this.getFrame().width.toFloat(),
        this.getFrame().height.toFloat(),
        getFillStyle().getColor()?.let { Color(it).value } ?: Color.Black.value
    )

    is Triangle -> ModelShape.Triangle(
        this.id,
        this.getFrame().left.toFloat(),
        this.getFrame().top.toFloat(),
        this.getFrame().width.toFloat(),
        this.getFrame().height.toFloat(),
        getFillStyle().getColor()?.let { Color(it).value } ?: Color.Black.value
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
