package ViewModel

import ViewModel.Styles.IFillStyle
import ViewModel.Styles.IStrokeStyle

interface IShape : IDrawable{
    val id: String
    fun getFrame(): RectI
    fun setFrame(frame: RectI)
    fun getStrokeStyle(): IStrokeStyle
    fun getFillStyle(): IFillStyle
}