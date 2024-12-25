package ViewModel

import View.ICanvas
import ViewModel.Styles.FillStyle
import ViewModel.Styles.StrokeStyle

abstract class Shape(
    override val id: String,
    strokeStyle: StrokeStyle,
    fillStyle: FillStyle,
    val isSelect: Boolean
): IShape, IShapeImpl {
    private var mStrokeStyle: StrokeStyle = strokeStyle
    private var mFillStyle: FillStyle = fillStyle

    override fun getFrame(): RectFloat = getFrameImpl()

    override fun setFrame(frame: RectFloat): IShape {
        val oldFrame = getFrame()

        val offsetLeft = frame.left - oldFrame.left
        val offsetTop = frame.top - oldFrame.top
        val scaleWidth = frame.width / when {
            oldFrame.width.toInt() == 0 -> 1f
            else -> oldFrame.width
        }
        val scaleHeight = frame.height / when {
            oldFrame.height.toInt() == 0 -> 1f
            else -> oldFrame.height
        }
        setFrameImpl(RectFloat(offsetLeft, offsetTop, scaleWidth, scaleHeight))
        return this
    }

    abstract override fun setFrameImpl(frame: RectFloat)

    abstract override fun hitTestImpl(x: Float, y: Float): Boolean

    abstract override fun getFrameImpl(): RectFloat

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
    }

    override fun hitTest(x: Float, y: Float): Boolean = hitTestImpl(x, y)

    companion object {
        const val SIZE_FRAME_POINTS = 8f
    }
}