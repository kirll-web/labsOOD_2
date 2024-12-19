package ViewModel

import View.ICanvas
import ViewModel.Styles.FillStyle
import ViewModel.Styles.StrokeStyle
import androidx.compose.ui.graphics.Color

abstract class Shape(
    override val id: String,
    strokeStyle: StrokeStyle,
    fillStyle: FillStyle,
    val isSelect: Boolean
): IShape, IShapeImpl {
    private var mStrokeStyle: StrokeStyle = strokeStyle
    private var mFillStyle: FillStyle = fillStyle

    override fun getFrame(): RectI = getFrameImpl()

    override fun setFrame(frame: RectI) {
        val oldFrame = getFrame()

        val offsetLeft = frame.left - oldFrame.left
        val offsetTop = frame.top - oldFrame.top
        val scaleWidth = frame.width / when {
            oldFrame.width == 0 -> 1
            else -> oldFrame.width
        }
        val scaleHeight = frame.height / when {
            oldFrame.height == 0 -> 1
            else -> oldFrame.height
        }
        setFrameImpl(RectI(offsetLeft, offsetTop, scaleWidth, scaleHeight))
    }

    abstract override fun setFrameImpl(frame: RectI)

    abstract override fun isPickImpl(x: Float, y: Float): Boolean

    abstract override fun getFrameImpl(): RectI

    abstract override fun drawImpl(canvas: ICanvas)

    override fun getStrokeStyle() = mStrokeStyle

    override fun getFillStyle() = mFillStyle

    override fun draw(canvas: ICanvas) = with(getFrame()){
        mFillStyle.getColor()?.let { color ->
            canvas.beginFill(color)
        }

        canvas.setStrokeColor(mStrokeStyle.getColor())
        canvas.setStrokeWidth(mStrokeStyle.getWidth())

        drawImpl(canvas)

        canvas.endFill()

        canvas.setStrokeColor(null)
        canvas.setStrokeWidth(0u)

        //fixme вынести отрисовку рамки в отдельный shape
        if (isSelect) {
            canvas.setStrokeColor(Color.Black.value)
            canvas.setStrokeWidth(5u)
            canvas.moveTo(left, top)
            canvas.lineTo(left + width, top)
            canvas.lineTo(left + width, top + height)
            canvas.lineTo(left, top + height)
            canvas.lineTo(left, top)
            canvas.endFill()
            canvas.setStrokeWidth(1u)
            canvas.beginFill(Color.White.value)
            canvas.drawEllipse(left + width, top + height, SIZE_FRAME_POINTS, SIZE_FRAME_POINTS)
            canvas.endFill()
            canvas.setStrokeColor(null)
            canvas.setStrokeWidth(0u)
        }
    }

    override fun isPick(x: Float, y: Float): Boolean = isPickImpl(x, y)

    companion object {
        const val SIZE_FRAME_POINTS = 7
    }
}