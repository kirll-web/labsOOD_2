package Canvas

data class Rect<T>(
    val left: T,
    val top: T,
    val width: T,
    val height: T
)
typealias RectD = Rect<Double>
