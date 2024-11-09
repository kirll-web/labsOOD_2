package org.command.modern_graphics_lib

import java.io.Closeable
import java.io.OutputStream

// Класс для современного рисования графики
class ModernGraphicsRenderer(
    private val strm: OutputStream
) {
    private var mDrawing = false

    // Этот метод должен быть вызван в начале рисования
    fun beginDraw() {
        if (mDrawing) {
            throw Exception("Drawing has already begun")
        }
        strm.write("<draw>".toByteArray())
        mDrawing = true
    }

    // Выполняет рисование линии
    fun drawLine(start: Point, end: Point) {
        if (!mDrawing) {
            throw Exception("DrawLine is allowed between BeginDraw()/EndDraw() only")
        }
        strm.write(
            (
                    "<line fromX=\\\"${start.x}\\\"" +
                            " fromY=\\\"${start.y}\\\"" +
                            " toX=\\\"${end.x}\\\" " +
                            "toY=\\\"${end.y}\\\"/>)"
                    ).toByteArray()
        )
    }

    // Этот метод должен быть вызван в конце рисования
    fun endDraw() {
        if (!mDrawing) {
            throw Exception("Drawing has not been started")
        }
        strm.write("</draw>".toByteArray())
        mDrawing = false
    }
}