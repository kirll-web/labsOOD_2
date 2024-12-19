package shape.Figures

import Canvas.ICanvas
import Canvas.RectD
import shape.*
import shape.Styles.FillStyle
import shape.Styles.StrokeStyle

class Ellipse(
    rect: RectD,
    strokeStyle: StrokeStyle,
    fillStyle: FillStyle
): Shape(strokeStyle, fillStyle) {
    private var mLeft = rect.left
    private var mTop = rect.top
    private var mWidth = rect.width
    private var mHeight = rect.height

    override fun setFrameImpl(frame: RectD) {
        mLeft += frame.left
        mTop += frame.top
        mWidth *= frame.width
        mHeight *= frame.height
    }

    override fun getFrameImpl() = RectD(mLeft, mTop, mWidth, mHeight)

    override fun drawImpl(canvas: ICanvas) {
        canvas.drawEllipse(
            mLeft,
            mTop,
            mWidth,
            mHeight
        )
    }
}