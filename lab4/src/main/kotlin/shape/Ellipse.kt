package shape

import Canvas.ICanvas
import Color.ShapeColor
import common.Point

class Ellipse(
    private val center: Point,
    private val verticalRadius: Double,
    private val horizontalRadius: Double,
    color: ShapeColor
) : Shape(color) {

    override fun draw(canvas: ICanvas) {
        super.draw(canvas)
        canvas.drawEllipse(
            center.x,
            center.y,
            horizontalRadius,
            verticalRadius
        )
    }

    fun getCenter() = center
    fun getVerticalRadius() = verticalRadius
    fun getHorizontalRadius() = horizontalRadius
}