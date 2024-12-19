package ViewModel.Figures

import View.ICanvas
import ViewModel.Point
import ViewModel.RectI
import ViewModel.Shape
import ViewModel.Styles.FillStyle
import ViewModel.Styles.StrokeStyle

class TrianglePoints(
    val leftBottom: Point, //left top
    val top: Point, // top
    val rightBottom: Point // right top
)

class Triangle(
    id: String,
    private var points: TrianglePoints,
    strokeStyle: StrokeStyle,
    fillStyle: FillStyle,
    isSelect: Boolean
): Shape(id, strokeStyle, fillStyle, isSelect) {
    private var mLeft = points.leftBottom.x
    private var mTop = points.top.y
    private var mWidth = points.rightBottom.x - mLeft
    private var mHeight = points.rightBottom.y - mTop


    override fun setFrameImpl(frame: RectI) {
        mLeft += frame.left
        mTop += frame.top
        mWidth += frame.width
        mHeight += frame.height

        points = TrianglePoints(
            leftBottom = Point(
                x = mLeft,
                y = mTop + mHeight
            ),
            top = Point(
                x = mLeft + mWidth / 2,
                y = mTop
            ),
            rightBottom = Point(
                x = mLeft + mWidth,
                y = mTop + mHeight
            )
        )
    }

    override fun isPickImpl(x: Float, y: Float): Boolean = with(points){
        // Вершины треугольника
        val ax = mLeft.toFloat()
        val ay = (mTop + mHeight).toFloat()
        val bx = (mLeft + mWidth).toFloat()
        val by = (mTop + mHeight).toFloat()
        val cx = (mLeft + mWidth / 2).toFloat()
        val cy = mTop.toFloat()

        // Функция для вычисления площади треугольника
        fun triangleArea(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float): Float {
            return Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2f)
        }

        // Площадь исходного треугольника
        val originalArea = triangleArea(ax, ay, bx, by, cx, cy)

        // Сумма площадей трёх треугольников
        val area1 = triangleArea(x, y, bx, by, cx, cy)
        val area2 = triangleArea(ax, ay, x, y, cx, cy)
        val area3 = triangleArea(ax, ay, bx, by, x, y)

        // Проверяем, равна ли сумма площадей исходной площади
        return (area1 + area2 + area3) == originalArea
    }

    override fun getFrameImpl(): RectI = RectI(
        mLeft, mTop, mWidth, mHeight
    )

    override fun drawImpl(canvas: ICanvas) {
        canvas.moveTo(points.leftBottom.x, points.leftBottom.y)
        canvas.lineTo(points.top.x, points.top.y)
        canvas.lineTo(points.rightBottom.x, points.rightBottom.y)
        canvas.lineTo(points.leftBottom.x, points.leftBottom.y)
    }


}