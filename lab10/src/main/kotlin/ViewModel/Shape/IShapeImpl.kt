package ViewModel

import View.ICanvas

interface IShapeImpl {
    fun setFrameImpl(frame: RectFloat)

    fun getFrameImpl(): RectFloat

    fun drawImpl(canvas: ICanvas)

    fun hitTestImpl(x: Float, y: Float): Boolean
}