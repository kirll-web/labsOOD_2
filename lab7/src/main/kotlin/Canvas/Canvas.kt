package Canvas

import RGBAColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import common.Point
import common.getOffset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

fun Double.dp(): Double = this.dp.value.toDouble()

class Canvas : ICanvas {
    private var mCursor = Point(0.0, 0.0)
    private var mShapes: List<Primitive> = listOf()
    private var mFillColor: RGBAColor? = DEFAULT_COLOR
    private var mStrokeColor: RGBAColor? = DEFAULT_COLOR
    private var mStrokeWidth = DEFAULT_STROKE_WIDTH
    val shapes
        get() = mShapes

    override fun setStrokeColor(color: RGBAColor?) {
        val it = mShapes.last()

        if(it is Primitive.Stroke) {
            mShapes = mShapes.minus(it)

            mShapes = mShapes.plus(Primitive.Stroke(
                color = color,
                strokeWidth = it.strokeWidth
            ))

        } else {
            mShapes = mShapes.plus(Primitive.Stroke(
                color =  color,
                strokeWidth = mStrokeWidth
            ))
        }
    }

    override fun setStrokeWidth(width: UInt) {
        val it = mShapes.last()

        if(it is Primitive.Stroke) {
            mShapes = mShapes.minus(it)

            mShapes = mShapes.plus(Primitive.Stroke(
                color = it.color,
                strokeWidth = width
            ))

        } else {
            mShapes = mShapes.plus(Primitive.Stroke(
                color = mStrokeColor,
                strokeWidth = width
            ))
        }
    }

    override fun beginFill(color: RGBAColor?) {
        mShapes = mShapes.plus(Primitive.Fill(
            color = color
        ))
    }

    override fun endFill() {
        mShapes = mShapes.plus(Primitive.Fill(
            color = null
        ))
    }

    override fun moveTo(x: Double, y: Double) {
        mCursor = Point(x.dp(), y.dp())
    }

    // Переставляет перо в точку с заданными координатами. Точка (x, y)
    // становится текущей позицией рисования.
    override fun lineTo(x: Double, y: Double) {
        val f = getOffset(mCursor.x.dp(), mCursor.y.dp())
        val s = getOffset(x.dp(), y.dp())
        mShapes = mShapes.plus(Primitive.Line(
            start = f,
            end = s
        ))

        moveTo(x, y)
    }
    /*
    * Соединяет текущую позицию рисования отрезком прямой с точкой (x, y).
    * Точка (x, y) становится текущей позицией рисования.
    * Линия рисуется текущим цветом линии. Линия рисуется текущим цветом.
    * */
    override fun drawEllipse(cx: Double, cy: Double, rx: Double, ry: Double) {
        mShapes = mShapes.plus(Primitive.Ellipse(
            topLeft = getOffset(cx.dp(), cy.dp()),
            rx = rx.dp(),
            ry = ry.dp()
        ))
    }

    fun draw(drawScope: DrawScope) {
        var path: Path? = null
        mShapes.forEach {
            when (it) {
                is Primitive.Ellipse -> {
                    if(path == null) it.draw(drawScope, mFillColor, mStrokeWidth, mStrokeColor)
                    else {
                        drawPath(path, drawScope)
                        path = null

                        it.draw(drawScope, mFillColor, mStrokeWidth, mStrokeColor)
                    }
                }

                is Primitive.Line -> {
                    if(path == null) path = Path()

                    path = it.draw(path!!)
                }

                is Primitive.Fill -> {
                    drawPath(path, drawScope)
                    path = null
                    mFillColor = it.color
                }
                is Primitive.Stroke -> {
                    drawPath(path, drawScope)
                    path = null

                    mStrokeColor = it.color
                    mStrokeWidth = it.strokeWidth
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
                    color = Color(color) // Цвет заливки
                )
            }
            mStrokeColor?.let { color ->
                drawScope.drawPath(
                    path = path,
                    color = Color(color),           // Цвет обводки
                    style = Stroke(mStrokeWidth.toFloat()) // Ширина обводки
                )
            }
        }
    }


    companion object {
        const val DEFAULT_STROKE_WIDTH = 1u
        const val DEFAULT_COLOR = 0xFF000000
    }
}