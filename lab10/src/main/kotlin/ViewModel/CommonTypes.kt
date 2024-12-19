package ViewModel

data class Rect<T>(
    val left: T,
    val top: T,
    val width: T,
    val height: T
)
typealias RectI = Rect<Int>