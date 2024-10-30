package shape

import Canvas.ICanvas
import common.Point
import Color.ShapeColor
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI


class RegularPolygon(
    private val center: Point,
    private val vertexCount: UInt,
    private val radius: Double,
    color: ShapeColor
) : Shape(color) {

    override fun draw(canvas: ICanvas) {
        super.draw(canvas)
        // Проверка входных данных
        require(vertexCount > 2U) { "Число сторон должно быть больше двух и нечетным." }

        val x = center.x
        val y = center.y

        val points = buildRegularPolygon(vertexCount.toInt(), center, radius)
        for (i in 0 until points.size - 1) {
            canvas.drawLine(points[i], points[i+1])
        }
        canvas.drawLine(points[points.size - 1], points[0])

    }

    fun getCenter() = center
    fun getVertexCount() = vertexCount
    fun getRadius() = radius

    private fun buildRegularPolygon(n: Int, center: Point, radius: Double): List<Point> {
        val theta = 2 * Math.PI / n
        val vertices = mutableListOf<Point>()

        for (i in 0 until n) {
            var x = center.x
            var y = center.y
            val angle = theta * i
            x += radius * cos(2 * Math.PI * i / n)
            y += radius * sin(2 * Math.PI * i / n)
            vertices.add(Point(x, y))
        }

        return vertices
    }

}