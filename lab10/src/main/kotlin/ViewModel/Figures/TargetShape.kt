package ViewModel.Figures

import View.ICanvas
import ViewModel.RectFloat
import ViewModel.Shape
import ViewModel.Styles.FillStyle
import ViewModel.Styles.StrokeStyle
import androidx.compose.ui.graphics.Color

class TargetShape(
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

    override fun hitTestImpl(x: Float, y: Float): Boolean {
        return x in mLeft..mLeft + mWidth  && y in mTop..mTop + mHeight
    }

    override fun getFrameImpl() = RectFloat(mLeft, mTop, mWidth, mHeight)

    override fun drawImpl(canvas: ICanvas) {
        canvas.setStrokeColor(Color.Black.value)
        canvas.setStrokeWidth(5u)
        canvas.moveTo(mLeft, mTop)
        canvas.lineTo(mLeft + mWidth, mTop)
        canvas.lineTo(mLeft + mWidth, mTop + mHeight)
        canvas.lineTo(mLeft, mTop + mHeight)
        canvas.lineTo(mLeft, mTop)
        canvas.endFill()
        canvas.setStrokeColor(Color.Black.value)
        canvas.setStrokeWidth(2u)
        canvas.beginFill(Color.White.value)
        canvas.drawEllipse(mLeft + mWidth, mTop + mHeight, SIZE_FRAME_POINTS, SIZE_FRAME_POINTS)
        canvas.endFill()
        canvas.setStrokeColor(null)
        canvas.setStrokeWidth(0u)
    }
}