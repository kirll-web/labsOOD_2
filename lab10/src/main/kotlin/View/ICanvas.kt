package View

import RGBAColor

interface ICanvas {
    fun setStrokeWidth(width: UInt)
    fun setStrokeColor(color: RGBAColor?)
    fun beginFill(color: RGBAColor?)
    fun endFill()
    fun moveTo(
        x: Int,
        y: Int
    )//. Переставляет перо в точку с заданными координатами. Точка (x, y) становится текущей позицией рисования.

    fun lineTo(
        x: Int,
        y: Int
    ) //. Соединяет текущую позицию рисования отрезком прямой с точкой (x, y). Точка (x, y) становится текущей позицией рисования. Линия рисуется текущим цветом линии. Линия рисуется текущим цветом.

    fun drawEllipse(
        cx: Int,
        cy: Int,
        rx: Int,
        ry: Int
    )//. Рисует эллипс с центром в точке (cx, cy), rx - горизонтальный радиус, ry - вертикальный радиус. Эллипс рисуется текущим цветом. Текущая позиция рисования не меняется.
}