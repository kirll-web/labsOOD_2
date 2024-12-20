package ViewModel

import ViewModel.Styles.IFillStyle
import ViewModel.Styles.IStrokeStyle

interface IShape : IDrawable{
    val id: String
    fun getFrame(): RectFloat
    fun setFrame(frame: RectFloat): IShape
    fun getStrokeStyle(): IStrokeStyle
    fun getFillStyle(): IFillStyle
    fun hitTest(x: Float, y: Float): Boolean
}