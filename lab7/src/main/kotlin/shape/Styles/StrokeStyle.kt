package shape.Styles

import RGBAColor


class StrokeStyle(
    width: UInt,
    color: RGBAColor?,
) : IStrokeStyle, BaseStyle(color) {
    private var mWidth = width

    override fun setWidth(width: UInt) {
        mWidth = width
    }

    override fun getWidth() = mWidth
}