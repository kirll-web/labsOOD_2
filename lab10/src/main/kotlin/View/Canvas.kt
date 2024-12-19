package View

import RGBAColor
import ViewModel.ICanvasViewModel
import ViewModel.Point
import ViewModel.getOffset
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

fun Int.dp() = this.dp.value.toInt()


class Canvas(
    private val viewModel: ICanvasViewModel
) : ICanvas {
    private val mViewModelState by viewModel.state
    private var mCursor = Point(0, 0)
    private var mShapes: List<Primitive> = listOf()
    private var mFillColor: RGBAColor? = DEFAULT_COLOR
    private var mStrokeColor: RGBAColor? = DEFAULT_COLOR
    private var mStrokeWidth = DEFAULT_STROKE_WIDTH
    val shapes
        get() = mShapes

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

    override fun moveTo(x: Int, y: Int) {
        mCursor = Point(x.dp(), y.dp())
    }

    override fun lineTo(x: Int, y: Int) {
        val f = getOffset(mCursor.x.dp(), mCursor.y.dp())
        val s = getOffset(x.dp(), y.dp())
        mShapes = mShapes.plus(
            Primitive.Line(
                start = f,
                end = s
            )
        )

        moveTo(x, y)
    }

    override fun drawEllipse(cx: Int, cy: Int, rx: Int, ry: Int) {
        mShapes = mShapes.plus(
            Primitive.Ellipse(
                topLeft = getOffset(cx.dp(), cy.dp()),
                rx = rx.dp(),
                ry = ry.dp()
            )
        )
    }

    @Composable
    fun draw() {
        Box(Modifier.fillMaxSize().background(Color.LightGray).pointerInput(Unit) {
            detectTapGestures { offset ->
                viewModel.selectShape(offset.x, offset.y)
            }
        }) {
            mShapes = emptyList()
            mViewModelState.shapes.values.forEach {
                it.draw(this@Canvas)
            }.apply {
                Canvas(
                    Modifier.fillMaxSize().pointerInput(Unit) {
                        detectDragGestures(
                             onDragStart = { dragAmount ->
                                 viewModel.onDragStart(dragAmount.x, dragAmount.y)
                             },
                            onDragCancel = {},
                            onDragEnd = {viewModel.onDragEnd()},
                            onDrag = { _, dragAmount ->
                                viewModel.onDrag(dragAmount.x, dragAmount.y)
                            }
                        )

                    }) {
                    viewModel.changeWindowSize(this.size.width, this.size.height)
                    var path: Path? = null
                    mShapes.forEach {
                        when (it) {
                            is Primitive.Ellipse -> {
                                if (path == null) it.draw(this, mFillColor, mStrokeWidth, mStrokeColor)
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
        val DEFAULT_COLOR = (0xFF000000).toULong() // fixme
    }
}