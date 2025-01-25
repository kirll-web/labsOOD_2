package org.command.Adapter

data class Color(
    val r: Byte,
    val g: Byte,
    val b: Byte,
    val a: Byte,
)

class Style(
    private val color: Color
) {
    fun getColor() = color
}

class Shape(
    private val style: Style
) {
    fun getStyle() = style
}

interface IStyle {
    fun getColor(): UInt //аналог Uint32 в плюсах https://kotlinlang.org/docs/unsigned-integer-types.html
}

interface IShape {
    fun getStyle(): IStyle
}

class StyleAdapter(private val adaptee: Style) : IStyle {
    override fun getColor(): UInt {
        val color = adaptee.getColor()
        return ((color.a.toUInt() shl 24) or
                (color.r.toUInt() shl 16) or
                (color.g.toUInt() shl 8) or
                color.b.toUInt())
    }
}

class ShapeAdapter(private val adaptee: Shape) : IShape {
    override fun getStyle(): IStyle {
        return StyleAdapter(adaptee.getStyle())
    }
}

fun main() {
    val shapeAdapter: IShape = ShapeAdapter(Shape(
        Style(
            Color(255.toByte(), g = 0.toByte(), b = 0.toByte(), a = 255.toByte()))
        )
    )

    println(shapeAdapter.getStyle().getColor())
}