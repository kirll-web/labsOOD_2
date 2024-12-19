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