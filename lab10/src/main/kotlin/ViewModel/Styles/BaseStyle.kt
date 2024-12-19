package ViewModel.Styles

import RGBAColor

open class BaseStyle(
    color: RGBAColor?
) {
    private var mColor: RGBAColor? = color
    open fun getColor() = mColor
    open fun setColor(color: RGBAColor?) { mColor = color }
}