package shape.Styles

import RGBAColor

interface IFillStyle {
    fun getColor(): RGBAColor?
    fun setColor(color: RGBAColor?)
}