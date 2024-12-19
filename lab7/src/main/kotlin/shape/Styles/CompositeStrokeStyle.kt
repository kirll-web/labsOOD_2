package shape.Styles

import RGBAColor


class CompositeStrokeStyle(
    val enumerator: (cb: (style: IStrokeStyle, index: Int) -> Unit) -> Unit,
) : IStrokeStyle, BaseStyle(0x0) {

    override fun getWidth(): UInt{
        var firstValue = 0u
        var isAllStylesSame = true
        enumerator { style, index ->
            if (!isAllStylesSame) return@enumerator
            val color = style.getWidth()
            if (index == 0) {
                firstValue = color
                return@enumerator
            }
            isAllStylesSame = firstValue == color
        }

        return firstValue
    }
    override fun setWidth(width: UInt) {
        enumerator { style, _ ->
            style.setWidth(width)
        }
    }

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
            isAllStylesSame = firstValue == color
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