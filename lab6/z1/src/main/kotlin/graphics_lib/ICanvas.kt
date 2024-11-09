package org.command.graphics_lib

interface ICanvas {
    // Ставит "перо" в точку x, y
    fun moveTo(x: Int, y: Int)
    // Рисует линию с текущей позиции, передвигая перо в точку x,y
    fun lineTo(x: Int, y: Int)
}