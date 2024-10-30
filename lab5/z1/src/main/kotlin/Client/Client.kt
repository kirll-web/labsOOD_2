package Client

import Core.Core
import Document.Document
import Document.IDocument
import DocumentItem.IConstDocumentItem
import Ext.isInt
import Ext.isListInt
import Image.IImage
import Menu.Menu
import Paragraph.IParagraph
import java.io.File

class Client {
    private val mMenu = Menu()
    private val mDocument: IDocument = Document()

    init {
        mMenu.addItem(
            "insert paragraph",
            "insertParagraph"
        ) { input ->
            insertParagraph(input)
        }

        mMenu.addItem(
            "insert image",
            "insertImage"
        ) { input ->
            insertImage(input)
        }

        mMenu.addItem(
            "setTitle",
            "setTitle"
        ) { input ->
            setTitle(input)
        }

        mMenu.addItem(
            "deleteItem",
            "deleteItem"
        ) { input ->
            deleteItem(input)
        }

        mMenu.addItem(
            "save",
            "save"
        ) { input ->
            save(input)
        }

        mMenu.addItem(
            "undo",
            "undo"
        ) { _ ->
            undo()
        }

        mMenu.addItem(
            "redo",
            "redo"
        ) { _ ->
            redo()
        }

        mMenu.addItem(
            "replace text",
            "replaceText"
        ) { input ->
            replaceText(input)
        }

        mMenu.addItem(
            "resize image",
            "resizeImage"
        ) { input ->
            resizeImage(input)
        }

        mMenu.addItem(
            "list",
            "list"
        ) { _ ->
            list()
        }

        mMenu.addItem(
            "help",
            "help"
        ) { _ ->
            help()
        }
    }

    fun run() = mMenu.run()


    private fun insertParagraph(input: String) {
        val args = input.trim().split(" ")
        if (args.size < AMOUNT_ARGS_INSERT_PARAGRAPH) {
            print("Args is not enough for insert text")
            return
        }

        val positionString = args[0]

        if(!positionString.isInt() || positionString != END) {
            print("Position is not a number")
            return
        }

        val position = parsePosition(positionString)
        val text = args.slice(3 until args.size).joinToString(" ")

        when {
            isPositionInRange(position) -> mDocument.insertParagraph(text, position)

            else -> {
                print("Position is not in range")
                return
            }
        }

    }

    private fun insertImage(input: String) {
        val args = input.trim().split(" ")
        if (args.size < AMOUNT_ARGS_INSERT_IMAGE) {
            print("Args is not enough for insert image")
            return
        }

        val positionString = args[0]

        if(!positionString.isInt() || positionString != END) {
            print("Position is not a number")
            return
        }

        val position = parsePosition(positionString)
        val width = args[1].toInt()
        val height = args[2].toInt()
        val path = args.slice(3 until args.size).joinToString(" ")

        when {
            isPositionInRange(position) -> mDocument.insertImage(path, width, height, position)

            else -> {
                print("Position is not in range")
                return
            }
        }
    }

    private fun deleteItem(input: String) {
        val args = input.trim().split(" ")
        if (args.size < AMOUNT_ARGS_DELETE_ITEM) {
            print("Args is not in range")
            return
        }

        val positionString = args[0]

        if(!positionString.isInt() || positionString != END) {
            print("Position is not a number")
            return
        }

        val position = parsePosition(positionString)

        when {
            isPositionInRange(position) -> {
                val item = mDocument.getItem(position).getImage()

                mDocument.deleteItem(mDocument.getItem(position))
            }

            else -> {
                print("Position is not in range")
                return
            }
        }
    }

    private fun resizeImage(input: String) {
        val args = input.trim().split(" ")
        if (args.size < AMOUNT_ARGS_RESIZE_IMAGE) {
            print("Args is not enough for resize image")
            return
        }
        val positionString = args[0]

        if(!positionString.isInt() || positionString != END) {
            print("Position is not a number")
            return
        }

        if(!listOf(args[1], args[2]).isListInt()) {
            print("Args is not a numbers")
            return
        }
        val position = parsePosition(positionString)
        val width = args[1].toInt()
        val height = args[2].toInt()

        when {
            isPositionInRange(position) -> {
                val item = mDocument.getItem(position).getImage()

                when {
                    item != null -> item.resize(width, height)
                    else -> {
                        print("This item is not an image")
                        return
                    }
                }
            }

            else -> {
                print("Position is not in range")
                return
            }
        }
    }

    private fun replaceText(input: String) {
        val args = input.trim().split(" ")
        if (args.size < AMOUNT_ARGS_REPLACE_TEXT) {
            print("Args is not enough for replace text")
            return
        }

        val positionString = args[0]

        if(!positionString.isInt() || positionString != END) {
            print("Position is not a number")
            return
        }

        val position = parsePosition(positionString)

        val text = args.slice(1 until  args.size).joinToString(" ")

        when {
            isPositionInRange(position) -> {
                val item = mDocument.getItem(position).getParagraph()

                when {
                    item != null -> item.setText(text)
                    else -> {
                        print("This item is not a paragraph")
                        return
                    }
                }
            }

            else -> {
                print("Position is not in range")
                return
            }
        }
    }

    private fun list() {
        println("Title: ${mDocument.getTitle()}")
        for (i in 0 until mDocument.getItemsCount()) {
            print("${i + 1}. ")
            val item = mDocument.getItem(i).getParagraph() ?: mDocument.getItem(i).getImage()
            when (item) {
                is IParagraph -> println("Paragraph: ${item.getText()}")
                is IImage -> println("Paragraph: ${item.getWidth()} ${item.getHeight()} ${item.getString()}")
            }
        }
    }

    private fun help() {
        mMenu.showMenu()
    }

    private fun setTitle(input: String) {
        val args = input.trim().split(" ")
        if (args.size < AMOUNT_ARGS_SET_TITLE) {
            print("Args is not enough for set title")
            return
        }

        mDocument.setTitle(args.joinToString(" "))
    }

    private fun undo() {
        when {
            mDocument.canUndo() -> mDocument.undo()
            else -> {
                println("Document cannot be undo now")
                return
            }
        }
    }


    private fun redo() {
        when {
            mDocument.canRedo() -> mDocument.redo()
            else -> {
                println("Document cannot be redo now")
                return
            }
        }
    }

    private fun save(input: String) {
        val args = input.trim().split(" ")
        if (args.size < AMOUNT_ARGS_SAVE) {
            print("Args is not enough for save")
            return
        }
    }

    private fun parsePosition(positionString: String) = when {
        positionString == END -> mDocument.getItemsCount() - 1
        else -> positionString.toInt()
    }

    private fun isPositionInRange(position: Int) = mDocument.getItemsCount() > position

    private fun createFolder()  {
        val directory = File(Core.generateRandomFileName(Core.MAX_LENGTH_TEMP_NAME))
        if (directory.exists()) {
            directory.delete()
        }

        directory.mkdirs()
    }

    companion object {
        const val AMOUNT_ARGS_INSERT_IMAGE = 4
        const val AMOUNT_ARGS_INSERT_PARAGRAPH = 2
        const val AMOUNT_ARGS_SET_TITLE = 1
        const val AMOUNT_ARGS_DELETE_ITEM = 1
        const val AMOUNT_ARGS_RESIZE_IMAGE= 3
        const val AMOUNT_ARGS_REPLACE_TEXT= 2
        const val AMOUNT_ARGS_SAVE = 1
        const val END = "end"
    }
}