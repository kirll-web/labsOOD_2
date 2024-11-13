package Document

import Command.ChangeStringCommand
import Command.DeleteItemCommand
import Command.InsertItemCommand
import Core.Core
import DocumentItem.ConstDocumentItem
import DocumentItem.DocumentItem
import History.History
import Image.IImage
import Image.Image
import Paragraph.IParagraph
import Paragraph.Paragraph
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path


class Document(private val mTempFolder: File) : IDocument {
    val mHistory = History()
    val mItems = mutableListOf<DocumentItem>()
    var mTitle = ""

    override fun insertParagraph(text: String, position: Int): IParagraph? {
        val item = DocumentItem(Paragraph(text, mHistory))

        mHistory.addAndExecuteCommand(InsertItemCommand(mItems, item, position))

        return item.getParagraph()
    }

    override fun insertImage(path: String, width: Int, height: Int, position: Int): IImage? {
        val file = File(path)
        if (!file.exists()) {
            print("The file $path does not exist")
            return null
        }

        val fileName = Core.generateRandomFileName(Core.MAX_LENGTH_TEMP_NAME)
        val extension = file.extension
        val to = mTempFolder.path + "/" + fileName + extension
        Files.copy(Path(path), Path(to), StandardCopyOption.REPLACE_EXISTING)

        val image = Image(Path(to), width, height, mHistory)
        val item = DocumentItem(image = image)

        mHistory.addAndExecuteCommand(InsertItemCommand(mItems, item, position))

        return image
    }

    override fun getItemsCount(): Int = mItems.size

    override fun getConstItem(index: Int): ConstDocumentItem? =  when {
        index >= mItems.size -> {
            println("Index is out of range")
            null
        }
        else -> mItems[index]
    }

    override fun getItem(index: Int): DocumentItem? = when {
        index >= mItems.size -> {
            println("Index is out of range")
            null
        }
        else -> mItems[index]
    }

    override fun deleteItem(index: DocumentItem) {
        when {
            mItems.contains(index) -> println("Item is not exist of range")
            else -> mHistory.addAndExecuteCommand(DeleteItemCommand(mItems, index))
        }
    }

    override fun getTitle()= mTitle

    override fun setTitle(title: String) {
        mHistory.addAndExecuteCommand(ChangeStringCommand(mTitle, title))
    }

    override fun canUndo(): Boolean = mHistory.canUndo()

    override fun undo() {
        mHistory.undo()
    }

    override fun canRedo(): Boolean = mHistory.canRedo()

    override fun redo() {
        mHistory.redo()
    }
}