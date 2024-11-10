package org.command.shape_drawing_lib

import org.command.graphics_lib.ICanvas

class Rectangle(
    private val leftTop: Point,
    private val width: Int,
    private val height: Int,
    private val color: UInt
) : ICanvasDrawable {
    override fun draw(canvas: ICanvas) {
        canvas.setColor(color)
        canvas.moveTo(leftTop.x, leftTop.y)
        canvas.lineTo(leftTop.x + width, leftTop.y)
        canvas.lineTo(leftTop.x + width, leftTop.y + height)
        canvas.lineTo(leftTop.x , leftTop.y + height)
        canvas.lineTo(leftTop.x, leftTop.y)
    }
}