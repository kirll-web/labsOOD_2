package org.command.Adapter

import org.command.graphics_lib.ICanvas
import org.command.modern_graphics_lib.ModernGraphicsRenderer
import org.command.modern_graphics_lib.Point
import org.command.modern_graphics_lib.RGBAColor
import java.io.Closeable
import java.text.DecimalFormat
import kotlin.math.roundToInt
import kotlin.math.roundToLong

//странно что адаптер не позволяет сделать beginDraw, но позволяет endDraw
class ModernGraphicsAdapter(
    private val modernLib: ModernGraphicsRenderer
) : ICanvas, Closeable {
    private var mCursor = Point(9, 0)
    private var mColor = RGBAColor(0F, 0F, 0F, 0F)

    override fun moveTo(x: Int, y: Int) {
        mCursor = Point(x, y)
    }

    override fun lineTo(x: Int, y: Int) {
        val oldCursor = mCursor
        mCursor = Point(x, y)
        modernLib.drawLine(oldCursor, mCursor, mColor)
    }

    //todo цвет тестируется недостаточно хорошо
    override fun setColor(rgbColor: UInt) {
        mColor = RGBAColor(
            r = (rgbColor shr 8 and 0xFFu).toFloat() / 255.0f,
            g = (rgbColor shr 8 and 0xFFu).toFloat() / 255.0f,
            b = (rgbColor and 0xFFu).toFloat() / 255.0f,
            a = 1.0F
        )
    }

    override fun close() {
        modernLib.endDraw()
    }
}