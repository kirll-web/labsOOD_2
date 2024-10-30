package Canvas

import Color.ShapeColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import common.Point
import common.getOffset

class Canvas : ICanvas {
    private var mColor = ShapeColor.RED
    private var mShapes: List<IPrimitive> = listOf()
    val shapes
        get() = mShapes

    override fun setColor(color: ShapeColor) {
        mColor = color
    }

    override  fun drawLine(from: Point, to: Point) {
        mShapes = mShapes.plus(Line(
            start = from.getOffset(),
            end = to.getOffset(),
            color = mColor
        ))
    }

    override fun draw(drawScope: DrawScope, rememberText: TextMeasurer) {
        mShapes.forEach {
            it.draw(drawScope, rememberText)
        }
    }

    override fun drawEllipse(cx: Double, cy: Double, rx: Double, ry: Double) {
        mShapes = mShapes.plus(Ellipse(
            topLeft = getOffset(cx, cy),
            rx = rx,
            ry = ry,
            color = mColor
        ))
    }
}