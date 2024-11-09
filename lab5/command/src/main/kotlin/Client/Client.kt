package org.command.Client

import Image.IImage
import Paragraph.IParagraph
import org.command.Document.Document
import org.command.Document.IDocument
import org.command.Ext.isInt
import org.command.Ext.isListInt
import org.command.Ext.saveToHtml
import org.command.Menu.Menu
import org.command.Menu.MenuItem
import java.io.File
import java.nio.file.Path
import kotlin.io.path.*


//todo уметь изобразить паттерн команда
//ресурсы должны удалять незамедлительно, если к изображению нельзя вернуться
//сделать ограниченную глубину команд - FIX
//подружить два предыдущих требования
//сделать склеивание соседних команд
//сделать экранирование символов
class Client {
    private val mMenu = Menu()
    private val mTempFolder: Path = createTempDirectory()
    private val mDocument: IDocument = Document(mTempFolder)

    init {

        mMenu.addItem(
            MenuItem(
                "InsertParagraph",
                "InsertParagraph <позиция>|end <текст параграфа>",
            ) { input ->
                insertParagraph(input)
            }
        )

        mMenu.addItem(
            MenuItem(
                "InsertImage",
                "InsertImage <позиция>|end <ширина> <высота> <путь к файлу изображения>",
            ) { input ->
                insertImage(input)
            }
        )

        mMenu.addItem(
            MenuItem(
                "SetTitle",
                "SetTitle <заголовок документа>",
            ) { input ->
                setTitle(input)
            }
        )

        mMenu.addItem(
            MenuItem(
                "List",
                "Выводит название и список элементов документа ",
            ) {
                list()
            }
        )


        mMenu.addItem(
            MenuItem(
                "ReplaceText",
                "ReplaceText <позиция> <текст параграфа>",
            ) { input ->
                replaceText(input)
            }
        )

        mMenu.addItem(
            MenuItem(
                "ResizeImage",
                "ResizeImage <позиция> <ширина> <высота>",
            ) { input ->
                resizeImage(input)
            }
        )

        mMenu.addItem(
            MenuItem(
                "DeleteItem",
                "DeleteItem <позиция>",
            ) { input ->
                deleteItem(input)
            }
        )

        mMenu.addItem(
            MenuItem(
                "Help",
                "Help",
            ) {
                help()
            }
        )

        mMenu.addItem(
            MenuItem(
                "Undo",
                "Undo",
            ) {
                undo()
            }
        )

        mMenu.addItem(
            MenuItem(
                "Redo",
                "Redo",
            ) {
                redo()
            }
        )

        mMenu.addItem(
            MenuItem(
                "save",
                "save <путь>",
            ) { input ->
                save(input)
            }
        )

    }

    fun run() {
        mMenu.showMenu()
        mMenu.run()
        mTempFolder.deleteIfExists()
    }

    private fun insertParagraph(input: String) {
        val (position, args) = try {
            parseInput(input, AMOUNT_ARGS_INSERT_PARAGRAPH)
        } catch (e: Exception) {
            println(e.message)
            return
        }

        val text = args.slice(1 until args.size).joinToString(" ")

        try {
            mDocument.insertParagraph(text, position)
        } catch (e: Exception) {
            println(e.message)
        }
    }


    private fun insertImage(input: String) {
        val (position, args) = try {
            parseInput(input, AMOUNT_ARGS_INSERT_IMAGE)
        } catch (e: Exception) {
            println(e.message)
            return
        }
        val width = args[1].toInt()
        val height = args[2].toInt()
        val path = args.slice(3 until args.size).joinToString(" ")

        try {
            mDocument.insertImage(path, width, height, position)
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun setTitle(input: String) {
        val args = input.trim().split(" ")
        if (args.size < AMOUNT_ARGS_SET_TITLE) {
            print("Args is not enough for set title")
            return
        }

        try {
            mDocument.setTitle(args.joinToString(" "))
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun list() {
        println("Title: ${mDocument.getTitle()}")
        for (i in 0 until mDocument.getItemsCount()) {
            print("${i + 1}. ")
            val item = mDocument.getItem(i)?.getParagraph() ?: mDocument.getItem(i)?.getImage()
            when (item) {
                is IParagraph -> println("Paragraph: ${item.getText()}")
                is IImage -> println("Paragraph: ${item.getWidth()} ${item.getHeight()} ${item.getString()}")
            }
        }
    }

    private fun replaceText(input: String) {
        val (position, args) = try {
            parseInput(input, AMOUNT_ARGS_REPLACE_TEXT)
        } catch (e: Exception) {
            println(e.message)
            return
        }

        val text = args.slice(1 until args.size).joinToString(" ")

        try {
            mDocument.getItem(position).getParagraph()?.setText(text)
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun resizeImage(input: String) {
        val (position, args) = try {
            parseInput(input, AMOUNT_ARGS_RESIZE_IMAGE)
        } catch (e: Exception) {
            println(e.message)
            return
        }

        if (!listOf(args[1], args[2]).isListInt()) {
            print("Args is not a numbers")
            return
        }
        val width = args[1].toInt()
        val height = args[2].toInt()

        try {
            mDocument.getItem(position).getImage()?.resize(width, height)
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun deleteItem(input: String) {
        val (position) = try {
            parseInput(input, AMOUNT_ARGS_DELETE_ITEM)
        } catch (e: Exception) {
            println(e.message)
            return
        }

        try {
            mDocument.getItem(position)?.let { mDocument.deleteItem(mDocument.getItem(position)) }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun help() = mMenu.showMenu()
    private fun undo() = mDocument.undo()
    private fun redo() = mDocument.redo()
    private fun save(input: String) {
        val args = input.trim().split(" ")
        if (args.size < AMOUNT_ARGS_SAVE) {
            print("Args is not enough for save")
            return
        }

        val path = Path(input)

        if (path.exists()) {
            println("File already exists")
            return
        }

        if (path.extension != "html") {
            println("Invalid extension")
            return
        }

        val parentPath = path.parent
        val imageFolderName = "${path.parent}/images"

        val imageFolder = File(imageFolderName)
        if (!imageFolder.exists()) imageFolder.mkdirs()


        saveToHtml(mDocument, path.toFile(), Path(imageFolderName))
    }

    private fun parseInput(input: String, amountArgs: Int): Pair<Int, List<String>> {
        val args = input.trim().split(" ")
        if (args.size < AMOUNT_ARGS_INSERT_PARAGRAPH) {
            throw IllegalArgumentException("Args is not enough for insert text")
        }

        val positionString = args[0]
        if (!positionString.isInt() && positionString != END) {
            throw IllegalArgumentException("Position is not a number")
        }

        val position = parsePosition(positionString)
        return position to args
    }

    private fun parsePosition(positionString: String) = when {
        positionString == END -> mDocument.getItemsCount()
        else -> positionString.toInt()
    }

    companion object {
        const val AMOUNT_ARGS_INSERT_IMAGE = 4
        const val AMOUNT_ARGS_INSERT_PARAGRAPH = 2
        const val AMOUNT_ARGS_SET_TITLE = 1
        const val AMOUNT_ARGS_DELETE_ITEM = 1
        const val AMOUNT_ARGS_RESIZE_IMAGE = 3
        const val AMOUNT_ARGS_REPLACE_TEXT = 2
        const val AMOUNT_ARGS_SAVE = 1
        const val END = "end"
    }
}