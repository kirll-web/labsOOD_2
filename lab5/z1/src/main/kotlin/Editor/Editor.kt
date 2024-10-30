package Editor

import DocumentItem.IConstDocumentItem
import DocumentItem.IDocumentItem
import Image.IImage
import Paragraph.IParagraph

class Editor: IEditor {

    private val

    override fun insertParagraph(input: String): IParagraph {
        val args = input.trim().split(" ")
        if(args.size < AMOUNT_ARGS_INSERT_PARAGRAPH) {
            throw IllegalArgumentException("Args is not enough for insert text")
        }
    }

    override fun insertImage(input: String): IImage {
        val args = input.trim().split(" ")
        if(args.size < AMOUNT_ARGS_INSERT_IMAGE) {
            throw IllegalArgumentException("Args is not enough for insert image")
        }
    }

    override fun getItemsCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getConstItem(index: Int): IConstDocumentItem {
        TODO("Not yet implemented")
    }

    override fun getItem(index: Int): IDocumentItem {
        TODO("Not yet implemented")
    }

    override fun deleteItem(index: IDocumentItem) {
        TODO("Not yet implemented")
    }

    override fun getTitle(): String {
        TODO("Not yet implemented")
    }

    override fun setTitle(input: String) {
        val args = input.trim().split(" ")
        if(args.size < AMOUNT_ARGS_SET_TITLE) {
            throw IllegalArgumentException("Args is not enough for set title")
        }
    }

    override fun canUndo(): Boolean {
        TODO("Not yet implemented")
    }

    override fun undo() {
        TODO("Not yet implemented")
    }

    override fun canRedo(): Boolean {
        TODO("Not yet implemented")
    }

    override fun redo(): Boolean {
        TODO("Not yet implemented")
    }

    override fun save(path: String) {
        TODO("Not yet implemented")
    }
    
    companion object {
        const val AMOUNT_ARGS_INSERT_IMAGE = 4
        const val AMOUNT_ARGS_INSERT_PARAGRAPH = 1
        const val AMOUNT_ARGS_SET_TITLE = 2
    }
}