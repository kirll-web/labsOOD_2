package ViewModel

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
    fun onDrag(x: Float, y: Float, deltaX: Float, deltaY: Float)
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
    private var windowWidth: Float,
    private var windowHeight: Float,
    private val defaultWidthShape: Float,
    private val defaultHeightShape: Float
) : IComposeCanvasViewModel, IMenuViewModel, ViewModel() {
    private val mSelectedShapeId = mutableStateOf<String?>(null)
    private val mCanvasState = mutableStateOf(CanvasState())
    private val viewModelScope = CoroutineScope(Job() + Dispatchers.IO)
    private var mDragEvent: OnDragEvent? = null


    sealed class OnDragEvent(open val startX: Float, open val startY: Float) {
        data class MOVE(override val startX: Float, override val startY: Float) :
            OnDragEvent(startX, startY)

        data class RESIZE(override val startX: Float, override val startY: Float) :
            OnDragEvent(startX, startY)
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
            it.hitTest(x, y)
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
        val startedDragShape = mCanvasState.value.shapes.values.findLast {
            it.hitTest(x, y) || (it.id == mSelectedShapeId.value && isHitResizebleCircle(x, y, it.getFrame()))
        }

        //fixme убрать двойное вычисление
        when (startedDragShape) {
            null -> unselectShape()
            else -> {
                if (startedDragShape.id != mSelectedShapeId.value) selectShape(x, y)
                mDragEvent = when {
                    startedDragShape.hitTest(x, y) ->
                        OnDragEvent.MOVE(
                            x - startedDragShape.getFrame().left,
                            y - startedDragShape.getFrame().top
                        )

                    isHitResizebleCircle(x, y, startedDragShape.getFrame()) ->
                        OnDragEvent.RESIZE(
                            x,
                            y
                        )

                    else -> null
                }
            }
        }
    }

    override fun onDrag(x: Float, y: Float, deltaX: Float, deltaY: Float) {
        val it = mDragEvent
        when (it) {
            is OnDragEvent.RESIZE -> resizeShape(x, y)
            is OnDragEvent.MOVE -> moveShape(x - it.startX, y - it.startY)
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
        windowWidth = width
        windowHeight = height
    }

    override fun addRectangle() {
        unselectShape()
        addShape(
            //fixme вынести в фабрику
            ModelShape.Rectangle(
                UUID.randomUUID().toString(),
                (windowWidth / 2 - defaultWidthShape / 2),
                (windowHeight / 2 - defaultHeightShape / 2),
                defaultWidthShape,
                defaultHeightShape,
                Color.Black.value
            )
        )
    }

    override fun addEllipse() {
        unselectShape()
        addShape(
            ModelShape.Ellipse(
                UUID.randomUUID().toString(),
                (windowWidth / 2 - defaultWidthShape / 2),
                (windowHeight / 2 - defaultHeightShape / 2),
                defaultWidthShape,
                defaultHeightShape,
                Color.Red.value
            )
        )
    }

    override fun addTriangle() {
        unselectShape()
        addShape(
            ModelShape.Triangle(
                UUID.randomUUID().toString(),
                (windowWidth / 2 - defaultWidthShape / 2),
                (windowHeight / 2 - defaultHeightShape / 2),
                defaultWidthShape,
                defaultHeightShape,
                Color.Green.value
            )
        )
    }


    private fun addShape(modelShape: ModelShape) {
        dataModel.addShape(modelShape)
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
        dataModel.removeShapeById(id)
    }

    override fun addImage() {
        TODO("Not yet implemented")
    }

    override fun undo() {
        dataModel.undo()
    }

    override fun redo() {
        dataModel.redo()
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
                val newDeltaX = when {
                    deltaX < 0 || deltaX + shape.getFrame().width > windowWidth -> shape.getFrame().left
                    else -> deltaX
                }
                val newDeltaY = when {
                    deltaY < 0 || deltaY + shape.getFrame().height > windowHeight -> shape.getFrame().top
                    else -> deltaY
                }

                dataModel.updateShape(
                    shape.setFrame(
                        RectFloat(
                            newDeltaX,
                            newDeltaY,
                            shape.getFrame().width,
                            shape.getFrame().height
                        )
                    ).toModelShape()
                )
            }
        }
    }

    private fun resizeShape(deltaX: Float, deltaY: Float) {
        mSelectedShapeId.value?.let { id ->
            mCanvasState.value.shapes[id]?.let { shape ->
                var newDeltaX = deltaX - shape.getFrame().left
                var newDeltaY = deltaY - shape.getFrame().top

                newDeltaX = when {
                    newDeltaX <= MIN_WIDTH_SHAPE || newDeltaX + shape.getFrame().left > windowWidth -> shape.getFrame().width
                    else -> newDeltaX
                }

                newDeltaY = when {
                    newDeltaY <= MIN_HEIGHT_SHAPE || newDeltaY + shape.getFrame().top > windowHeight -> shape.getFrame().height
                    else -> newDeltaY
                }

                shape.setFrame(
                    RectFloat(
                        shape.getFrame().left,
                        shape.getFrame().top,
                        newDeltaX,
                        newDeltaY
                    )
                )

                dataModel.updateShape(shape.toModelShape())
            }
        }
    }

    private fun isHitResizebleCircle(x: Float, y: Float, frame: RectFloat) =
        (x > frame.left + frame.width &&
                x < frame.left + frame.width + Shape.SIZE_FRAME_POINTS &&
                y > frame.top + frame.height &&
                y < frame.top + frame.height + Shape.SIZE_FRAME_POINTS)

    companion object {
        private const val MIN_WIDTH_SHAPE = 5f
        private const val MIN_HEIGHT_SHAPE = 5f
    }
}

fun ModelShape.toShape(
    isSelect: Boolean? = null
) = when (this) {
    is ModelShape.Rectangle -> Rectangle(
        this.id,
        RectFloat(
            x,
            y,
            width,
            height
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
        RectFloat(
            x,
            y,
            width,
            height
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
                x, y + height
            ),
            top = Point(
                x + width / 2, y
            ),
            rightBottom = Point(
                x + width,
                y + height
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
        this.getFrame().left,
        this.getFrame().top,
        this.getFrame().width,
        this.getFrame().height,
        getFillStyle().getColor()?.let { Color(it).value } ?: Color.Black.value
    )

    is Ellipse -> ModelShape.Ellipse(
        this.id,
        this.getFrame().left,
        this.getFrame().top,
        this.getFrame().width,
        this.getFrame().height,
        getFillStyle().getColor()?.let { Color(it).value } ?: Color.Black.value
    )

    is Triangle -> ModelShape.Triangle(
        this.id,
        this.getFrame().left,
        this.getFrame().top,
        this.getFrame().width,
        this.getFrame().height,
        getFillStyle().getColor()?.let { Color(it).value } ?: Color.Black.value
    )

    else -> throw IllegalArgumentException("Unknowed figure")
}

fun IShape.copy(
    x: Float? = null, y: Float? = null,
    width: Float? = null,
    height: Float? = null,
    color: Color? = null,
    isSelect: Boolean? = null,
): IShape {
    return when (this) {
        is Rectangle -> Rectangle(
            id,
            RectFloat(
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
            RectFloat(
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
