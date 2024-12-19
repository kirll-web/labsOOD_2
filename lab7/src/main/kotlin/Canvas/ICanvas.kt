package Canvas

import RGBAColor

interface ICanvas {
    fun setStrokeWidth(width: UInt)
    fun setStrokeColor(color: RGBAColor?)
    fun beginFill(color: RGBAColor?)
    fun endFill()
    fun moveTo(
        x: Double,
        y: Double
    )//. Переставляет перо в точку с заданными координатами. Точка (x, y) становится текущей позицией рисования.

    fun lineTo(
        x: Double,
        y: Double
    ) //. Соединяет текущую позицию рисования отрезком прямой с точкой (x, y). Точка (x, y) становится текущей позицией рисования. Линия рисуется текущим цветом линии. Линия рисуется текущим цветом.

    fun drawEllipse(
        cx: Double,
        cy: Double,
        rx: Double,
        ry: Double
    )//. Рисует эллипс с центром в точке (cx, cy), rx - горизонтальный радиус, ry - вертикальный радиус. Эллипс рисуется текущим цветом. Текущая позиция рисования не меняется.
}