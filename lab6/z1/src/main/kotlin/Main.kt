package org.command

import org.command.Adapter.ModernGraphicsAdapter
import org.command.Adapter.ModernGraphicsClassAdapter
import org.command.graphics_lib.Canvas
import org.command.modern_graphics_lib.ModernGraphicsRenderer
import org.command.shape_drawing_lib.CanvasPainter
import org.command.shape_drawing_lib.Point
import org.command.shape_drawing_lib.Rectangle
import org.command.shape_drawing_lib.Triangle
import java.util.*

fun paintPicture(painter: CanvasPainter) {
    val triangle = Triangle(
        Point(10, 15),
        Point(100, 300),
        Point(150, 250),
        0x3F3F3Fu
    )
    val rectangle = Rectangle(
        Point(30, 40),
        width = 18,
        height = 24,
        0x000000u
    )

    painter.draw(rectangle)
    painter.draw(triangle)
}

fun paintPictureOnCanvas() {
    val simpleCanvas = Canvas()
    val painter = CanvasPainter(simpleCanvas)
    paintPicture(painter)
}

fun paintPictureOnModernGraphicsRenderer() {
    val renderer = ModernGraphicsRenderer(System.out)
    renderer.beginDraw()
    ModernGraphicsAdapter(renderer).use { adapter ->
        val painter = CanvasPainter(adapter)

        paintPicture(painter)
    }

    ModernGraphicsClassAdapter(System.out).use { adapter ->
        adapter.beginDraw()
        val painter = CanvasPainter(adapter)
        paintPicture(painter)
    }
}

//TODO НАПИСАТЬ ЮНИТ ТЕСТЫ.  МОЖНО ПРОТЕСТИТЬ НА EXCEPTION. ПРИДУМАТЬ, КАК ПРОТЕСТИТЬ ВЫВОДИМЫЕ ДАННЫЕ
// МОЖНО ВЫВОДИТЬ ВСЁ В ФАЙЛ И ПРОВЕРЯТЬ НА СООТВЕТСТВИЕ
fun main() {
    println("Should we use new API (y)?")
    val userInput = readln()
    when {
        userInput.lowercase(Locale.getDefault()) == "y" -> paintPictureOnModernGraphicsRenderer()
        else -> paintPictureOnCanvas()
    }
}