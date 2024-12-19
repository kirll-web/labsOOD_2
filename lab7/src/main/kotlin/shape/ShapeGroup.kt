package shape

import Canvas.ICanvas
import Canvas.RectD
import shape.Styles.CompositeFillStyle
import shape.Styles.CompositeStrokeStyle
import shape.Styles.IFillStyle
import shape.Styles.IStrokeStyle

class ShapeGroup: IShapeGroup {
    private val mStrokeStyle = CompositeStrokeStyle { cb ->
        enumerateStrokeStyles(cb)
    }

    private val mFillStyle = CompositeFillStyle { cb ->
        enumerateFillStyles(cb)
    }
    private val mShapes = mutableListOf<IShape>()
    override fun getShapesCount() = mShapes.size

    override fun insertShape(shape: IShape, position: UInt){
        if (position.toInt() > mShapes.size) throw IllegalArgumentException("position > size of group")
        mShapes.add(position.toInt(), shape)
    }

    override fun getShapeAtIndex(index: UInt) : IShape {
        if (index.toInt() > mShapes.size) throw IllegalArgumentException("position > size of group")
        return mShapes[index.toInt()]
    }

    override fun removeShapeAtIndex(index: UInt) {
        if (index.toInt() > mShapes.size) throw IllegalArgumentException("position > size of group")
        mShapes.removeAt(index.toInt())
    }

    override fun getFrame(): RectD? {
        if (mShapes.isEmpty()) {
            return null
        }

        var leftTopX = Double.MAX_VALUE
        var leftTopY = Double.MAX_VALUE
        var rightBottomX = Double.MIN_VALUE
        var rightBottomY = Double.MIN_VALUE

        for (shape in mShapes) {

            val shapeFrame = shape.getFrame() ?: continue
            val shapeLeftTopX = shapeFrame.left
            val shapeLeftTopY = shapeFrame.top
            val shapeRightBottomX = shapeFrame.left + shapeFrame.width
            val shapeRightBottomY = shapeFrame.top + shapeFrame.height

            leftTopX = if (shapeLeftTopX < leftTopX) shapeLeftTopX else leftTopX
            leftTopY = if (shapeLeftTopY < leftTopY) shapeLeftTopY else leftTopY
            rightBottomX =
                if (shapeRightBottomX > rightBottomX) shapeRightBottomX else rightBottomX
            rightBottomY =
                if (shapeRightBottomY > rightBottomY) shapeRightBottomY else rightBottomY
        }


        return RectD(
            leftTopX,
            leftTopY,
            rightBottomX - leftTopX,
            rightBottomY - leftTopY
        )
    }

    override fun setFrame(frame: RectD) {
        if (mShapes.isEmpty()) return

        val groupFrame = getFrame()  ?: return

        val scaleWidth = frame.width / groupFrame.width
        val scaleHeight = frame.height / groupFrame.height

        for (shape in mShapes) {
            val shapeFrame = shape.getFrame() ?: continue
            //проблема с пустыми группами

            val shapeLeftInGroupFrame = shapeFrame.left - groupFrame.left
            val shapeLeftDueScaling = shapeLeftInGroupFrame * scaleWidth

            val shapeTopInGroupFrame = shapeFrame.top - groupFrame.top
            val shapeTopDueScaling = shapeTopInGroupFrame * scaleHeight

            shape.setFrame(
                RectD(
                    frame.left + shapeLeftDueScaling,
                    frame.top + shapeTopDueScaling,
                    shapeFrame.width * scaleWidth,
                    shapeFrame.height * scaleHeight,
                )
            )
        }
    }

    override fun getStrokeStyle() = mStrokeStyle

    //сеттеры для стилей не должно быть

    override fun getFillStyle() = mFillStyle

    override fun draw(canvas: ICanvas) {
        //begin fill делает отдельная фигура
        canvas.beginFill(getFillStyle().getColor())
        //у групп не должно быть переопределение цвета
        canvas.setStrokeWidth(getStrokeStyle().getWidth())
        canvas.setStrokeColor(getStrokeStyle().getColor())
        mShapes.forEach {
            it.draw(canvas)
        }

        canvas.endFill()
    }

    private fun enumerateStrokeStyles(cb: (style: IStrokeStyle, index: Int) -> Unit)  {
        for ((index, shape) in mShapes.withIndex()) {
            cb(shape.getStrokeStyle(), index)
        }
    }
    private fun enumerateFillStyles(cb: (style: IFillStyle, index: Int) -> Unit)  {
        for ((index, shape) in mShapes.withIndex()) {
            cb(shape.getFillStyle(), index)
        }
    }
}