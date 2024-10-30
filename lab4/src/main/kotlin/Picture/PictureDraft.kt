package Picture

import shape.Shape

class PictureDraft: IPictureDraft {
    private var mShapes: List<Shape> = listOf()

    fun addShape(shape: Shape) {
        mShapes = mShapes.plus(shape)
    }

    override fun getShapeCount() = mShapes.count()

    override fun getShape(index: Int) = mShapes[index]
}