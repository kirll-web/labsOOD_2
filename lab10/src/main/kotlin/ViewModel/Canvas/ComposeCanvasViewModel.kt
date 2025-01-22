package ViewModel.Canvas

import Models.IModelShapes
import Models.ModelShapesEvent
import ViewModel.*
import ViewModel.Figures.*
import ViewModel.Styles.FillStyle
import ViewModel.Styles.StrokeStyle
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

interface IComposeCanvasViewModel {
    val state:  StateFlow<CanvasState>
    val viewModelScope: CoroutineScope
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
    private val dataModel: IModelShapes,
    private var windowWidth: Float,
    private var windowHeight: Float,
    private val mapper: Mapper,
    private val mSelectedShapeId: MutableState<String?>
) : IComposeCanvasViewModel, ViewModel() {
    private val mCanvasState = MutableStateFlow(CanvasState())
    override  val viewModelScope = CoroutineScope(Job() + Dispatchers.IO)
    private var mDragEvent: OnDragEvent? = null


    sealed class OnDragEvent(open val startX: Float, open val startY: Float) {
        data class MOVE(override val startX: Float, override val startY: Float) :
            OnDragEvent(startX, startY)

        data class RESIZE(override val startX: Float, override val startY: Float) :
            OnDragEvent(startX, startY)
    }

    override val state: StateFlow<CanvasState> = mCanvasState

    init {
        viewModelScope.launch {
            delay(500)
            dataModel.shapes.onEach {
                mCanvasState.value = mapper.mapToCanvasState(mCanvasState, mSelectedShapeId, it)
            }.launchIn(viewModelScope)

            dataModel.events.onEach {
                println("event")
                when (it){
                    is ModelShapesEvent.SelectShape -> mSelectedShapeId.value = it.id
                    else -> Unit
                }.also {
                    mCanvasState.value = mapper.mapToCanvasState(mCanvasState, mSelectedShapeId)
                }
            }.launchIn(viewModelScope)

        }

    }

    override fun selectShape(x: Float, y: Float) {
        var selectedShape = mCanvasState.value.shapes.values.findLast {
            val isTargetShape = it is TargetShape && isHitResizebleCircle(
                x,
                y,
                it.getFrame()
            )
            if (!isTargetShape) (it !is TargetShape && it.hitTest(x, y)) else isTargetShape
        }

        if (selectedShape is TargetShape) selectedShape =
            mCanvasState.value.shapes[mSelectedShapeId.value]

        when (selectedShape?.id) {
            null -> unselectShape()

            mSelectedShapeId.value -> return

            else -> {
                mSelectedShapeId.value = selectedShape.id
                mCanvasState.value = CanvasState(
                    shapes = mCanvasState.value.shapes.toMutableMap().apply {
                        this.values.findLast { it is TargetShape }?.let {  oldTargetShape ->
                            this.remove(oldTargetShape.id)
                        }


                        val id = mSelectedShapeId.value
                        val shape = this[id]

                        if (id != null && shape != null) {
                            val idFrame = UUID.randomUUID().toString()
                            val frame = shape.getFrame()
                            this[idFrame] = TargetShape(
                                idFrame,
                                RectFloat(
                                    frame.left,
                                    frame.top,
                                    frame.width,
                                    frame.height
                                ),
                                StrokeStyle(0u, null),
                                FillStyle(null),
                                false
                            )
                        }
                        println(this)

                    }.toMutableMap()
                )
                println(mCanvasState.value.shapes)
            }
        }
    }

    override fun onDragStart(x: Float, y: Float) {
        var startedDragShape = mCanvasState.value.shapes.values.findLast {
            val isTargetShape = it is TargetShape && isHitResizebleCircle(
                x,
                y,
                it.getFrame()
            )
            if (!isTargetShape) (it !is TargetShape && it.hitTest(x, y)) else isTargetShape
        }


        if (startedDragShape is TargetShape) startedDragShape =
            mCanvasState.value.shapes[mSelectedShapeId.value]

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


    override fun changeWindowSize(width: Float, height: Float) {
        windowWidth = width
        windowHeight = height
    }

    private fun unselectShape() {
        val shapeId = mSelectedShapeId.value
        mSelectedShapeId.value = null
        shapeId?.let { id ->
            val shape = mCanvasState.value.shapes[id]
            shape?.let {
                mCanvasState.value = CanvasState(
                    shapes = mCanvasState.value.shapes.toMutableMap().also { shapes ->
                        shapes.values.findLast { it is TargetShape }?.id?.let { id ->
                            shapes.remove(id)
                        }
                    }
                )
            }
        }
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

                mapper.mapToModelShape(
                    shape.setFrame(
                        RectFloat(
                            newDeltaX,
                            newDeltaY,
                            shape.getFrame().width,
                            shape.getFrame().height
                        )
                    )
                )?.let {
                    mSelectedShapeId.value = shape.id
                    dataModel.moveShape(it)
                }
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

                mapper.mapToModelShape(shape)?.let {
                    mSelectedShapeId.value = shape.id
                    dataModel.resizeShape(it)
                }
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


        is Image -> Image(
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
            isSelect ?: this.isSelect,
            imgUrl,
            imageBitmap
        )

        else -> {
            throw IllegalArgumentException("Unknow type figure")
        }
    }
}
