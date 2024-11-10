package org.command.modern_graphics_lib

import java.io.Closeable
import java.io.OutputStream

class RGBAColor(
    r: Float,
    g: Float,
    b: Float,
    a: Float
) {
    private var mA: Float
    private var mR: Float
    private var mG: Float
    private var mB: Float

    init {
        if(
            (r > 1.0 || r < 0.0) || (g > 1.0 || g < 0.0) ||
            (b > 1.0 || b < 0.0) || (a > 1.0 || a < 0.0)
        ) {
            throw Exception("Arg must me > 0.0 and < 1.0")
        }

        mR = r
        mG = g
        mB = b
        mA = a
    }

    val a
        get() = mA

    val r
        get() = mR
    val g
        get() = mG
    val b
        get() = mB
}

// Класс для современного рисования графики
open class ModernGraphicsRenderer(
    private val strm: OutputStream
): Closeable {
    private var mDrawing = false

    // Этот метод должен быть вызван в начале рисования
    fun beginDraw() {
        if (mDrawing) {
            throw Exception("Drawing has already begun")
        }
        strm.write("<draw>\n".toByteArray())
        mDrawing = true
    }

    // Выполняет рисование линии
    fun drawLine(start: Point, end: Point, color: RGBAColor) {
        if (!mDrawing) {
            throw Exception("DrawLine is allowed between BeginDraw()/EndDraw() only")
        }
        strm.write(
            (
                    "\t<line fromX=\"${start.x}\"" +
                            " fromY=\"${start.y}\"" +
                            " toX=\"${end.x}\" " +
                            "toY=\"${end.y}\">" +
                            "<color r=\"${color.r}\" g=\"${color.g}\" b=\"${color.b}\" a=\"${color.a}\" />" +
                            "</line>)\n"
                    ).toByteArray()
        )
    }

    // Этот метод должен быть вызван в конце рисования
    fun endDraw() {
        if (!mDrawing) {
            throw Exception("Drawing has not been started")
        }
        strm.write("</draw>\n".toByteArray())
        mDrawing = false
    }

    override fun close() {
        endDraw()
    }
}