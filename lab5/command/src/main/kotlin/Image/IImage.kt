package Image

import java.nio.file.Path

interface IImage {
    // Возвращает путь относительно каталога документа
    fun getString(): String

    // Ширина изображения в пикселях
    fun getWidth(): Int

    // Высота изображения в пикселях
    fun getHeight(): Int

    // Изменяет размер изображения
    fun resize(width: Int, height: Int)
}