package View

import RGBAColor
import androidx.compose.ui.graphics.ImageBitmap

interface ICanvas {
    fun setStrokeWidth(width: UInt)
    fun setStrokeColor(color: RGBAColor?)
    fun beginFill(color: RGBAColor?)
    fun endFill()
    fun moveTo(
        x: Float,
        y: Float
    )//. Переставляет перо в точку с заданными координатами. Точка (x, y) становится текущей позицией рисования.

    fun lineTo(
        x: Float,
        y: Float
    ) //. Соединяет текущую позицию рисования отрезком прямой с точкой (x, y). Точка (x, y) становится текущей позицией рисования. Линия рисуется текущим цветом линии. Линия рисуется текущим цветом.

    fun drawEllipse(
        cx: Float,
        cy: Float,
        rx: Float,
        ry: Float
    )//. Рисует эллипс с центром в точке (cx, cy), rx - горизонтальный радиус, ry - вертикальный радиус. Эллипс рисуется текущим цветом. Текущая позиция рисования не меняется.

    fun drawImage(
        width: Float,
        height: Float,
        imgUrl: ImageBitmap
    )
}