package shape

import Canvas.RectD
import shape.Styles.IFillStyle
import shape.Styles.IStrokeStyle

interface IShape : IDrawable{
    fun getFrame(): RectD?
    fun setFrame(frame: RectD)
    fun getStrokeStyle(): IStrokeStyle
    fun getFillStyle(): IFillStyle
}