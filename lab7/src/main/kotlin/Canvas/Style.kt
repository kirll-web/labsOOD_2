package Canvas

import RGBAColor

class Style {
    private var mIsEnabled = false
    private var mColor = 0xFF000000
    fun isEnabled() = mIsEnabled
    fun enable(enable: Boolean) {
        mIsEnabled = enable
    }

    fun getColor(): RGBAColor = mColor

    fun setColor(color: RGBAColor) {
        mColor = color
    }
}