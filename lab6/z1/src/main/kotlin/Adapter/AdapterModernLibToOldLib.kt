package org.command.Adapter

import org.command.graphics_lib.ICanvas
import org.command.modern_graphics_lib.ModernGraphicsRenderer
import org.command.modern_graphics_lib.Point
import java.io.Closeable

class AdapterModernLibToOldLib(
    private val modernLib: ModernGraphicsRenderer
): ICanvas, Closeable {
    private var mDrawing = false
    private var mCursor = Point(0, 0)

    override fun moveTo(x: Int, y: Int) {
        if (!mDrawing) {
            modernLib.beginDraw()
            mDrawing = true
        }
        mCursor = Point(x,y)
    }

    override fun lineTo(x: Int, y: Int) {
        val oldCursor = mCursor
        mCursor = Point(x,y)
        modernLib.drawLine(oldCursor, mCursor)
    }

    override fun close() {
        modernLib.endDraw()
    }
}