package Slide

import Canvas.ICanvas
import RGBAColor
import shape.*
import java.util.stream.IntStream.range

class Slide : ISlide {
    private var mWidth = 1920.0
    private var mHeight = 1080.0
    private var mShapes: IShapes = ShapeGroup()
    private var mBackgroundColor = 0xFFFFFFFF
    override fun getWidth(): Double = mWidth

    override fun getHeight(): Double = mHeight


    override fun getShapes(): IShapes = mShapes

    override fun draw(canvas: ICanvas) {
        for (i in range(0, mShapes.getShapesCount())) {
            mShapes.getShapeAtIndex(i.toUInt()).draw(canvas)
        }
    }

    fun removeShapeAtIndex(index: UInt) {
        mShapes.removeShapeAtIndex(index)
    }


    fun getShapesCount(): Int = mShapes.getShapesCount()

    fun getBackgroundColor(): RGBAColor = mBackgroundColor


    fun getShapeAtIndex(index: UInt) = mShapes.getShapeAtIndex(index)

    fun insertShape(shape: IShape, position: UInt) {
        mShapes.insertShape(shape, position)
    }


    fun setBackgroundColor(color: RGBAColor) {
        mBackgroundColor = color
    }

    fun drawSelf(canvas: ICanvas) {
        canvas.beginFill(getBackgroundColor())
        canvas.moveTo(0.0, 0.0)
        canvas.lineTo( mWidth, 0.0)
        canvas.lineTo(mWidth, mHeight)
        canvas.lineTo(0.0, mHeight)
    }

}