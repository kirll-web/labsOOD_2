package shape

import Canvas.ICanvas
import Canvas.RectD
import shape.Styles.*

abstract class Shape(
    strokeStyle: StrokeStyle,
    fillStyle: FillStyle
): IShape, IShapeImpl {
    private val mStrokeStyle: StrokeStyle = strokeStyle
    private val mFillStyle: FillStyle = fillStyle
    override fun getFrame(): RectD = getFrameImpl()

    override fun setFrame(frame: RectD) {
        val oldFrame = getFrame()

        val offsetLeft = frame.left - oldFrame.left
        val offsetTop = frame.top - oldFrame.top
        val scaleWidth = frame.width / oldFrame.width
        val scaleHeight = frame.height / oldFrame.height

        setFrameImpl(RectD(offsetLeft, offsetTop, scaleWidth, scaleHeight))
    }

    abstract override fun setFrameImpl(frame: RectD)

    abstract override fun getFrameImpl(): RectD

    abstract override fun drawImpl(canvas: ICanvas)

    override fun getStrokeStyle() = mStrokeStyle

    override fun getFillStyle() = mFillStyle

    override fun draw(canvas: ICanvas) {
        mFillStyle.getColor()?.let { color ->
            canvas.beginFill(color)
        }

        canvas.setStrokeColor(mStrokeStyle.getColor())
        canvas.setStrokeWidth(mStrokeStyle.getWidth())

        drawImpl(canvas)

        canvas.endFill()
    }

}