package Image

import org.command.Command.ICommandExecutor
import org.command.Command.ResizeImageCommand

class Image(
    private val path: String,
    width: Int,
    height: Int,
    private val historyController: ICommandExecutor
) : IImage {
    private var mWidth = width
    private var mHeight = height


    // Возвращает путь относительно каталога документа
    override fun getString(): String = path

    // Ширина изображения в пикселях
    override fun getWidth() = mWidth

    // Высота изображения в пикселях
    override fun getHeight() = mHeight

    // Изменяет размер изображения
    override fun resize(width: Int, height: Int) {
        val oldWidth = mWidth
        val oldHeight = mHeight

        historyController.addAndExecuteCommand(
            ResizeImageCommand(
                { this.mWidth = width; this.mHeight = height },
                { mWidth = oldWidth; mHeight = oldHeight }
            )
        )
    }

}
