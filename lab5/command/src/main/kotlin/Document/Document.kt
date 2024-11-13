package org.command.Document

import DocumentItem.ConstDocumentItem
import DocumentItem.DocumentItem
import Image.IImage
import Image.Image
import Paragraph.IParagraph
import Paragraph.Paragraph
import org.command.Command.DeleteItemCommand
import org.command.Command.InsertCommand
import org.command.Command.SetTitleCommand
import org.command.Ext.MAX_LENGTH_TEMP_NAME
import org.command.Ext.generateRandomFileName
import org.command.History.History
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path

class Document(
    private val tempFolder: Path
): IDocument {
    private var mItems = mutableListOf<DocumentItem>()
    private var mHistory = History()
    private var mTitle: String = ""
    
    override fun insertParagraph(text: String, position: Int): IParagraph {
        checkPosition(position)
        val item = DocumentItem(Paragraph(text, mHistory))

        mHistory.addAndExecuteCommand(InsertCommand(mItems, item, position))

        return item.getParagraph()!!

    }

    override fun insertImage(path: String, width: Int, height: Int, position: Int): IImage {
        checkPosition(position)
        val file = File(path)
        if (!file.exists()) {
            throw IllegalArgumentException("The file $path does not exist")
        }

        val extension = file.name
        val to = "${tempFolder}/${generateRandomFileName(MAX_LENGTH_TEMP_NAME)}$extension"
        Files.copy(Path(file.path), Path(to), StandardCopyOption.REPLACE_EXISTING)

        val image = Image(to, height, height, mHistory)
        val item = DocumentItem(image = image)

        mHistory.addAndExecuteCommand(InsertCommand(mItems, item, position))

        return image
    }

    override fun getItemsCount(): Int = mItems.size

    override fun getConstItem(index: Int): ConstDocumentItem {
        checkPosition(index)
        return mItems[index]
    }

    override fun getItem(index: Int): DocumentItem {
        checkPosition(index)

        return mItems[index]
    }

    override fun deleteItem(index: DocumentItem) {
        when {
            mItems.contains(index) -> throw IllegalArgumentException("Item is not exist of range")
            else -> mHistory.addAndExecuteCommand(DeleteItemCommand(mItems, index))
        }
    }

    override fun getTitle(): String = mTitle

    override fun setTitle(title: String) {
        val oldTitle = mTitle
        mHistory.addAndExecuteCommand(SetTitleCommand({
            mTitle = title
        }, {
            mTitle = oldTitle
        }))
    }

    override fun canUndo() = mHistory.canUndo()

    override fun undo() {
        if (mHistory.canUndo()) {
            mHistory.undo()
        }
    }

    override fun canRedo() = mHistory.canRedo()

    override fun redo() {
        mHistory.redo()
    }

    private fun checkPosition(index: Int) {
        if(index > mItems.size) {
            throw IllegalArgumentException("Position is not in range")
        }
    }
}