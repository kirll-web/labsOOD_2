package shape

import Canvas.ICanvas
import common.Point
import Color.ShapeColor


class Rectangle(
    private val leftTop: Point,
    private val rightBottom: Point,
    color: ShapeColor
) : Shape(color) {

    override fun draw(canvas: ICanvas) {
        super.draw(canvas)
        canvas.drawLine(leftTop, Point(leftTop.x + rightBottom.x, leftTop.y))
        canvas.drawLine(Point(leftTop.x + rightBottom.x, leftTop.y), rightBottom)
        canvas.drawLine(rightBottom, Point(leftTop.x, rightBottom.y))
        canvas.drawLine(Point(leftTop.x, rightBottom.y), leftTop)
    }

    fun getLeftTop() = leftTop
    fun getRightBottom() = rightBottom
}