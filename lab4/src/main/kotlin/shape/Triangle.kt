package shape

import Canvas.ICanvas
import Color.ShapeColor
import common.Point

class Triangle(
    private val v1: Point,
    private val v2: Point,
    private val v3: Point,
    color: ShapeColor
) : Shape(color) {

    override fun draw(canvas: ICanvas) {
        super.draw(canvas)
        canvas.drawLine(v1, v2)
        canvas.drawLine(v2, v3)
        canvas.drawLine(v3, v1)
    }

    fun getVertex1() = v1

    fun getVertex2() = v2

    fun getVertex3() = v3
}