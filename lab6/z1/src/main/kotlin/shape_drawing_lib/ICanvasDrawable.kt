package org.command.shape_drawing_lib

import org.command.graphics_lib.ICanvas

// Интерфейс объектов, которые могут быть нарисованы на холсте из graphics\_lib
interface ICanvasDrawable {
    fun draw(canvas: ICanvas)
}
