package org.command.Adapter

import org.command.graphics_lib.ICanvas
import org.command.modern_graphics_lib.ModernGraphicsRenderer
import org.command.modern_graphics_lib.Point
import org.command.modern_graphics_lib.RGBAColor
import java.io.Closeable
import java.io.OutputStream
import java.text.DecimalFormat
import kotlin.math.roundToInt

class ModernGraphicsClassAdapter(
    strm: OutputStream,
): ICanvas, ModernGraphicsRenderer(strm) {
    private var mCursor = Point(0, 0)
    private var mColor = RGBAColor(0F, 0F, 0F, 0F)

    override fun moveTo(x: Int, y: Int) {
        mCursor = Point(x,y)
    }

    override fun lineTo(x: Int, y: Int) {
        val oldCursor = mCursor
        mCursor = Point(x,y)
        drawLine(oldCursor, mCursor, mColor)
    }

    override fun setColor(rgbColor: UInt) {
        mColor = RGBAColor(
            r = (rgbColor shr 16 and 0xFFu).toFloat() / 255.0f,
            g = (rgbColor shr 8 and 0xFFu).toFloat() / 255.0f,
            b = (rgbColor and 0xFFu).toFloat() / 255.0f,
            a = 1.0F
        )
    }
}