package shape.Styles

import RGBAColor


class CompositeFillStyle(
    private val enumerator: ((style: IFillStyle, index: Int) -> Unit) -> Unit,
) : IFillStyle, BaseStyle(0x0) {
    override fun getColor(): RGBAColor? {
        var firstValue: RGBAColor? = null
        var isAllStylesSame = true
        enumerator { style, index ->
            if (!isAllStylesSame) return@enumerator
            val color = style.getColor()
            if (index == 0) {
                firstValue = color
                return@enumerator
            }

            isAllStylesSame = isAllStylesSame && firstValue == color
        }

        return when {
            isAllStylesSame -> firstValue
            else -> null
        }
    }

    override fun setColor(color: RGBAColor?) {
        enumerator { style, _ ->
            style.setColor(color)
        }
    }
}