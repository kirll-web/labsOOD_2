package ViewModel.Figures

import View.ICanvas
import ViewModel.RectFloat
import ViewModel.Shape
import ViewModel.Styles.FillStyle
import ViewModel.Styles.StrokeStyle


class Ellipse(
    id: String,
    rect: RectFloat,
    strokeStyle: StrokeStyle,
    fillStyle: FillStyle,
    isSelect: Boolean
): Shape(id, strokeStyle, fillStyle, isSelect) {
    private var mLeft = rect.left
    private var mTop = rect.top
    private var mWidth = rect.width
    private var mHeight = rect.height

    override fun setFrameImpl(frame: RectFloat) {
        mLeft += frame.left
        mTop += frame.top
        mWidth *= frame.width
        mHeight *= frame.height
    }

    override fun getFrameImpl() = RectFloat(mLeft, mTop, mWidth, mHeight)

    override fun drawImpl(canvas: ICanvas) {
        canvas.drawEllipse(
            mLeft,
            mTop,
            mWidth,
            mHeight
        )
    }

    override fun hitTestImpl(x: Float, y: Float): Boolean {
        val centerX = mLeft + mWidth / 2
        val centerY = mTop + mHeight / 2

        // Радиусы эллипса
        val a = mWidth / 2
        val b = mHeight / 2

        // Проверяем, находится ли точка внутри эллипса
        val normalizedX = (x - centerX) / a
        val normalizedY = (y - centerY) / b
        println("$x $y $centerX $centerY $a $b $normalizedX $normalizedY")
        return normalizedX * normalizedX + normalizedY * normalizedY < 1
    }
}