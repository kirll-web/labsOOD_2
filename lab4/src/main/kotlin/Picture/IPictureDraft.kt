package Picture

import shape.Shape

interface IPictureDraft {
    fun getShapeCount(): Int
    fun getShape(index: Int): Shape
}