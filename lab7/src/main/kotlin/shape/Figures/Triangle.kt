package shape.Figures

import Canvas.ICanvas
import Canvas.RectD
import common.Point
import shape.Styles.FillStyle
import shape.IShape
import shape.Shape
import shape.Styles.StrokeStyle
import kotlin.math.abs

class TrianglePoints(
    val leftBottom: Point, //left top
    val top: Point, // top
    val rightBottom: Point // right top
)

class Triangle(
    private var points: TrianglePoints,
    strokeStyle: StrokeStyle,
    fillStyle: FillStyle
): Shape(strokeStyle, fillStyle) {
    private var mLeft = points.leftBottom.x
    private var mTop = points.top.y
    private var mWidth = points.rightBottom.x - mLeft
    private var mHeight = points.rightBottom.y - mTop


    override fun setFrameImpl(frame: RectD) {
        // Найти текущие границы треугольника
        // Вычислить коэффициенты масштабирования
        mLeft += frame.left
        mTop += frame.top
        mWidth *= frame.width
        mHeight *= frame.height


        // Масштабировать и переместить точки
        val leftBottom = Point(
            x = mLeft,
            y = mTop + mHeight
        )
        val top = Point(
            x = mLeft + mWidth / 2,
            y = mTop
        )
        val rightBottom = Point(
            x = mLeft + mWidth,
            y = mTop + mHeight
        )

        mWidth = points.rightBottom.x - mLeft
        mHeight = points.rightBottom.y - mTop

        points = TrianglePoints(
            leftBottom,top,rightBottom
        )
    }

    override fun getFrameImpl(): RectD = RectD(
        mLeft, mTop, mWidth, mHeight
    )

    override fun drawImpl(canvas: ICanvas) {
        canvas.moveTo(points.leftBottom.x, points.leftBottom.y)
        canvas.lineTo(points.top.x, points.top.y)
        canvas.lineTo(points.rightBottom.x, points.rightBottom.y)
        canvas.lineTo(points.leftBottom.x, points.leftBottom.y)
    }
}