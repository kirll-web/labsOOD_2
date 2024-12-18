package ViewModel

import View.ICanvas

interface IShapeImpl {
    fun setFrameImpl(frame: RectI)

    fun getFrameImpl(): RectI

    fun drawImpl(canvas: ICanvas)
}