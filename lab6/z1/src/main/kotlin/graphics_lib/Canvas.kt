package org.command.graphics_lib

// Реализация холста для рисования
class Canvas : ICanvas {
    override fun moveTo(x: Int,y: Int) {
        println("MoveTo ($x,$y)")
    }
    override fun lineTo(x: Int,y: Int) {
        println("LineTo ($x,$y)")
    }
}
