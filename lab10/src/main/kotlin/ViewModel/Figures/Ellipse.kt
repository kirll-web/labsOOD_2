package ViewModel.Figures

import View.ICanvas
import ViewModel.RectI
import ViewModel.Shape
import ViewModel.Styles.FillStyle
import ViewModel.Styles.StrokeStyle


class Ellipse(
    id: String,
    rect: RectI,
    strokeStyle: StrokeStyle,
    fillStyle: FillStyle,
    isSelect: Boolean
): Shape(id, strokeStyle, fillStyle, isSelect) {
    private var mLeft = rect.left
    private var mTop = rect.top
    private var mWidth = rect.width
    private var mHeight = rect.height

    override fun setFrameImpl(frame: RectI) {
        mLeft += frame.left
        mTop += frame.top
        mWidth += frame.width
        mHeight += frame.height
    }

    override fun getFrameImpl() = RectI(mLeft, mTop, mWidth, mHeight)

    override fun drawImpl(canvas: ICanvas) {
        canvas.drawEllipse(
            mLeft,
            mTop,
            mWidth,
            mHeight
        )
    }
}