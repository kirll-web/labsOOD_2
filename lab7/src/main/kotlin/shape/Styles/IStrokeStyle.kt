package shape.Styles

import RGBAColor

interface IStrokeStyle {
    fun getColor(): RGBAColor?
    fun setColor(color: RGBAColor?)
    fun getWidth(): UInt
    fun setWidth(width: UInt)
}