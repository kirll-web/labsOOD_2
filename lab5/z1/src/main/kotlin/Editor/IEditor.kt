package Editor

import DocumentItem.IConstDocumentItem
import DocumentItem.IDocumentItem
import Image.IImage
import Paragraph.IParagraph

interface IEditor {
    fun insertParagraph(input: String): IParagraph

    fun insertImage(input: String): IImage

    fun getItemsCount(): Int

    fun getConstItem(index: Int): IConstDocumentItem
    fun getItem(index: Int): IDocumentItem

    fun deleteItem(index: IDocumentItem)

    fun getTitle(): String
    fun setTitle(input: String)

    fun canUndo(): Boolean
    fun undo()

    fun canRedo(): Boolean
    fun redo(): Boolean

    fun save(path: String)
}