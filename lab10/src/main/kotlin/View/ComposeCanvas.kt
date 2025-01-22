package View

import Legacy.ImageDialog2.getOffset
import RGBAColor
import ViewModel.Canvas.IComposeCanvasViewModel
import ViewModel.Point
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ComposeCanvas(
    private val viewModel: IComposeCanvasViewModel
) : ICanvas {
    private var mCursor = Point(0f, 0f)
    private var mShapes: List<Primitive> by mutableStateOf(emptyList())
    private var mFillColor: RGBAColor? = DEFAULT_COLOR
    private var mStrokeColor: RGBAColor? = DEFAULT_COLOR
    private var mStrokeWidth = DEFAULT_STROKE_WIDTH


    init {
        viewModel.viewModelScope.launch {
            delay(500)
            viewModel.state.onEach {
                delay(10)
                mShapes = emptyList()
                it.shapes.values.forEach { shape ->
                    shape.draw(this@ComposeCanvas)
                }
            }.launchIn(viewModel.viewModelScope)
        }
    }

    override fun setStrokeColor(color: RGBAColor?) {
        val it = mShapes.lastOrNull()

        if (it is Primitive.Stroke) {
            mShapes = mShapes.minus(it)

            mShapes = mShapes.plus(
                Primitive.Stroke(
                    color = color,
                    strokeWidth = it.strokeWidth
                )
            )

        } else {
            mShapes = mShapes.plus(
                Primitive.Stroke(
                    color = color,
                    strokeWidth = mStrokeWidth
                )
            )
        }
    }

    override fun setStrokeWidth(width: UInt) {
        val it = mShapes.last()

        if (it is Primitive.Stroke) {
            mShapes = mShapes.minus(it)

            mShapes = mShapes.plus(
                Primitive.Stroke(
                    color = it.color,
                    strokeWidth = width
                )
            )
        } else {
            mShapes = mShapes.plus(
                Primitive.Stroke(
                    color = mStrokeColor,
                    strokeWidth = width
                )
            )
        }
    }

    override fun beginFill(color: RGBAColor?) {
        mShapes = mShapes.plus(
            Primitive.Fill(
                color = color
            )
        )
    }

    override fun endFill() {
        mShapes = mShapes.plus(
            Primitive.Fill(
                color = null
            )
        )
    }

    override fun moveTo(x: Float, y: Float) {
        mCursor = Point(x, y)
    }

    override fun lineTo(x: Float, y: Float) {
        val f = getOffset(mCursor.x.toInt(), mCursor.y.toInt())
        val s = getOffset(x.toInt(), y.toInt())
        mShapes = mShapes.plus(
            Primitive.Line(
                start = f,
                end = s
            )
        )

        moveTo(x, y)
    }

    override fun drawEllipse(cx: Float, cy: Float, rx: Float, ry: Float) {
        mShapes = mShapes.plus(
            Primitive.Ellipse(
                topLeft = getOffset(cx.toInt(), cy.toInt()),
                rx = rx.toInt(),
                ry = ry.toInt()
            )
        )
    }


    override fun drawImage(width: Float, height: Float, imageBitmap: ImageBitmap) {
        mShapes = mShapes.plus(
            Primitive.Image(Offset(mCursor.x, mCursor.y), width, height, imageBitmap)
        )
    }

    @Composable
    fun draw() {
        Box(Modifier.fillMaxSize().background(Color.White).pointerInput(Unit) {
            detectTapGestures { offset ->
                viewModel.selectShape(offset.x, offset.y)
            }
        }) {
            Canvas(
                Modifier.fillMaxSize().graphicsLayer().pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { dragAmount ->
                            viewModel.onDragStart(dragAmount.x, dragAmount.y)
                        },
                        onDragCancel = {},
                        onDragEnd = { viewModel.onDragEnd() },
                        onDrag = { c, dragAmount ->
                            viewModel.onDrag(
                                c.position.x,
                                c.position.y,
                                dragAmount.x,
                                dragAmount.y
                            )
                        }
                    )
                }) {
                viewModel.changeWindowSize(this.size.width, this.size.height)
                var path: Path? = null

                mShapes.forEach {
                    when (it) {
                        is Primitive.Ellipse -> {
                            if (path == null) it.draw(
                                this,
                                mFillColor,
                                mStrokeWidth,
                                mStrokeColor
                            )
                            else {
                                drawPath(path, this)
                                path = null

                                it.draw(this, mFillColor, mStrokeWidth, mStrokeColor)
                            }
                        }

                        is Primitive.Line -> {
                            if (path == null) path = Path()

                            path = it.draw(path!!)
                        }

                        is Primitive.Fill -> {
                            drawPath(path, this)
                            path = null
                            mFillColor = it.color
                        }

                        is Primitive.Image -> {
                            if (path == null) it.draw(this)
                            else {
                                drawPath(path, this)
                                path = null

                                it.draw(this)
                            }
                        }

                        is Primitive.Stroke -> {
                            drawPath(path, this)
                            path = null

                            mStrokeColor = it.color
                            mStrokeWidth = it.strokeWidth
                        }
                    }
                }
            }
        }
    }


    private fun drawPath(p: Path?, drawScope: DrawScope) {
        var path = p
        if (path != null) {
            path = path.apply {
                close()
            }

            mFillColor?.let { color ->
                drawScope.drawPath(
                    path = path,
                    color = Color(color)
                )
            }
            mStrokeColor?.let { color ->
                drawScope.drawPath(
                    path = path,
                    color = Color(color),
                    style = Stroke(mStrokeWidth.toFloat())
                )
            }
        }
    }

    companion object {
        const val DEFAULT_STROKE_WIDTH = 1u
        val DEFAULT_COLOR = Color.Black.value
    }
}