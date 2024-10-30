package IShapeFactory

import shape.Shape


interface IShapeFactory {
    fun createShape(descr: String):  Shape?
}