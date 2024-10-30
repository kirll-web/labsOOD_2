package shape

import Canvas.ICanvas
import Color.ShapeColor


abstract class Shape(
    private val color: ShapeColor,
) {
    open fun draw(canvas: ICanvas) {
        canvas.setColor(color)
    }
}