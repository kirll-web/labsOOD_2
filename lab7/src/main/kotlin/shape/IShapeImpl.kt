package shape

import Canvas.ICanvas
import Canvas.RectD

interface IShapeImpl {
    fun setFrameImpl(frame: RectD)

    fun getFrameImpl(): RectD

    fun drawImpl(canvas: ICanvas)
}