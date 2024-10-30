package Canvas

import Color.ShapeColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import common.Point

interface ICanvas {
    fun setColor(c: ShapeColor)// . Изменяет текущий цвет рисования.

    fun drawLine(from: Point, to: Point)

    fun drawEllipse(
        cx: Double,
        cy: Double,
        rx: Double,
        ry: Double
    )//. Рисует эллипс с центром в точке (cx, cy), rx - горизонтальный радиус, ry - вертикальный радиус. Эллипс рисуется текущим цветом. Текущая позиция рисования не меняется.


    fun draw(drawScope: DrawScope, rememberText: TextMeasurer)
}