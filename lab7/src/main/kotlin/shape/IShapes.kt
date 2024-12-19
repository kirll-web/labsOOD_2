package shape

interface IShapes {
    fun getShapesCount(): Int
    fun insertShape(shape: IShape, position: UInt)
    fun getShapeAtIndex(index: UInt): IShape
    fun removeShapeAtIndex(index: UInt)
}