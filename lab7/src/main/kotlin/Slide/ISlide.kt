package Slide

import shape.IDrawable
import shape.IShapeGroup
import shape.IShapes

interface ISlide: IDrawable {
    fun getWidth(): Double
    fun getHeight(): Double

    fun getShapes(): IShapes
}