package Document

import DocumentItem.IConstDocumentItem
import DocumentItem.IDocumentItem
import Image.IImage
import Paragraph.IParagraph

class Document : IDocument {
    override fun insertParagraph(text: String, position: Int?): IParagraph {
        TODO("Not yet implemented")
    }

    override fun insertImage(path: String, width: Int, height: Int, position: Int?): IImage {
        TODO("Not yet implemented")
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

    override fun setTitle(title: String) {
        TODO("Not yet implemented")
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

}