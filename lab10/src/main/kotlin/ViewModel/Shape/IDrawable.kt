package ViewModel

import View.ICanvas


interface IDrawable {
    fun draw(canvas: ICanvas)
}