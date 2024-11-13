package org.command.shape_drawing_lib

import org.command.graphics_lib.ICanvas


class Triangle(
    private val p1: Point,
    private val p2: Point,
    private val p3: Point,
    private val color: UInt
) : ICanvasDrawable {
    override fun draw(canvas: ICanvas) {
        canvas.setColor(color)
        canvas.moveTo(p1.x, p1.y)
        canvas.lineTo(p2.x, p2.y)
        canvas.lineTo(p3.x, p3.y)
        canvas.lineTo(p1.x, p1.y)
    }
}