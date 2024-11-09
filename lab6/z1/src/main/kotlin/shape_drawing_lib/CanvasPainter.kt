package org.command.shape_drawing_lib

import org.command.graphics_lib.ICanvas

class CanvasPainter(
    private val canvas: ICanvas
) {
    fun draw(drawable: ICanvasDrawable) {
        drawable.draw(canvas)
    }
}