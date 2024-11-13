package org.command.graphics_lib

// Реализация холста для рисования
class Canvas : ICanvas {
    override fun moveTo(x: Int,y: Int) {
        println("MoveTo ($x,$y)")
    }
    override fun lineTo(x: Int,y: Int) {
        println("LineTo ($x,$y)")
    }

    override fun setColor(rgbColor: UInt) {
       println(uintToHexColor(rgbColor))
    }

    companion object {
        fun uintToHexColor(color: UInt): String {
            return String.format("#%06X", color.toInt() and 0xFFFFFF)
        }
    }
}
