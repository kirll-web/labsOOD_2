import Image.IImage
import org.command.Document.Document
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createTempDirectory
import kotlin.io.path.pathString
import kotlin.test.assertEquals

//дописать тесты
class Test {
    @Test
    fun testTitle() {
        val file = Path("file")
        val doc = Document(file)
        val testTitle = "Title1"
        doc.setTitle(testTitle)

        assertEquals(doc.getTitle(), testTitle)
    }

    @Test
    fun testTitleWithHistory1() {
        val file = Path("file")
        val doc = Document(file)
        val defaultTitle = doc.getTitle()
        val testTitle1 = "Title1"
        val testTitle2 = "Title2"
        val testTitle3 = "Title3"
        doc.setTitle(testTitle1)
        assertEquals(doc.getTitle(), testTitle1)
        doc.setTitle(testTitle2)
        assertEquals(doc.getTitle(), testTitle2)
        doc.setTitle(testTitle3)
        assertEquals(doc.getTitle(), testTitle3)

        doc.undo()
        assertEquals(doc.getTitle(), testTitle2)
        doc.undo()
        assertEquals(doc.getTitle(), testTitle1)
        doc.undo()
        assertEquals(doc.getTitle(), defaultTitle)
        doc.undo()
        assertEquals(doc.getTitle(), defaultTitle)

        doc.redo()
        assertEquals(doc.getTitle(), testTitle1)

        doc.redo()
        assertEquals(doc.getTitle(), testTitle2)
        doc.redo()
        assertEquals(doc.getTitle(), testTitle3)
        doc.redo()
        assertEquals(doc.getTitle(), testTitle3)
    }

    @Test
    fun testExceptionParagraph() {
        val file = Path("file")
        val doc = Document(file)

        assertThrows<Exception> {
            doc.insertParagraph("Paragraph1", 1)
        }
        assertThrows<Exception> {
            doc.insertParagraph("Paragraph1", 5)
        }
        assertThrows<Exception> {
            doc.insertParagraph("Paragraph1", 1)
        }

        assertDoesNotThrow {
            doc.insertParagraph("Paragraph1", 0)
            doc.insertParagraph("Paragraph1", 1)
            doc.insertParagraph("Paragraph1", 2)
            doc.insertParagraph("Paragraph1", 1)
        }
    }

    @Test
    fun testExceptionImage() {
        val file = Path(createTempDirectory().pathString)
        val doc = Document(file)

        assertThrows<Exception> {
            doc.insertImage("C:\\Users\\regha\\Рабочий стол\\2\\2.jpg", 50, 50, 1)
        }
        assertThrows<Exception> {
            doc.insertImage("C:\\Users\\regha\\Рабочий стол\\2\\2.jpg", 50, 50, 5)
        }
        assertThrows<Exception> {
            doc.insertImage("C:\\Users\\regha\\Рабочий стол\\2\\2.jpg", 50, 50, 1)
        }

        assertDoesNotThrow {
            doc.insertImage("C:\\Users\\regha\\Рабочий стол\\2\\2.jpg", 50, 50, 0)
            doc.insertImage("C:\\Users\\regha\\Рабочий стол\\2\\2.jpg", 50, 50, 1)
            doc.insertImage("C:\\Users\\regha\\Рабочий стол\\2\\2.jpg", 50, 50, 2)
            doc.insertImage("C:\\Users\\regha\\Рабочий стол\\2\\2.jpg", 50, 50, 1)
        }
    }

    @Test
    fun testParagraphWithHistory() {
        val file = Path("file")
        val doc = Document(file)

        val p1 = "Paragraph1"
        val p2 = "Paragraph2"
        val p3 = "Paragraph3"

        doc.insertParagraph(p1, 0)
        assertEquals(doc.getItemsCount(), 1)
        assertEquals(doc.getItem(0).getParagraph()?.getText(), p1)

        doc.insertParagraph(p2, 1)
        assertEquals(doc.getItemsCount(), 2)
        assertEquals(doc.getItem(1).getParagraph()?.getText(), p2)

        doc.insertParagraph(p3, 0)
        assertEquals(doc.getItemsCount(), 2)
        assertEquals(doc.getItem(0).getParagraph()?.getText(), p3)

        doc.undo()
        assertEquals(doc.getItemsCount(), 2)
        assertEquals(doc.getItem(0).getParagraph()?.getText(), p1)
        assertEquals(doc.getItem(1).getParagraph()?.getText(), p2)

        doc.undo()
        assertEquals(doc.getItemsCount(), 1)
        assertEquals(doc.getItem(0).getParagraph()?.getText(), p1)

        doc.redo()
        assertEquals(doc.getItemsCount(), 2)
        assertEquals(doc.getItem(0).getParagraph()?.getText(), p1)
        assertEquals(doc.getItem(1).getParagraph()?.getText(), p2)

        doc.redo()
        assertEquals(doc.getItemsCount(), 2)
        assertEquals(doc.getItem(0).getParagraph()?.getText(), p3)
    }

    @Test
    fun testDeleteImageInTempDirectory() {
        val file = Path("C:\\Users\\regha\\Рабочий стол\\2\\test")
        val doc = Document(file)
        val pathImage = "C:\\Users\\regha\\Рабочий стол\\2\\2.jpg"
        val list = mutableListOf<IImage>()
        list.add(doc.insertImage(pathImage, 150, 150, 0))
        list.add(doc.insertImage(pathImage, 150, 150, 1))
        list.add(doc.insertImage(pathImage, 150, 150, 2))
        doc.undo()
        doc.undo()
        list.add(doc.insertImage(pathImage, 150, 150, 1))

        assertEquals(File(list[0].getString()).exists(), true)
        assertEquals(File(list[1].getString()).exists(), false)
        assertEquals(File(list[2].getString()).exists(), false)
        assertEquals(File(list[3].getString()).exists(), true)
    }

    @Test
    fun testDeleteImageInTempDirectory1() {
        val file = Path("C:\\Users\\regha\\Рабочий стол\\2\\test")
        val doc = Document(file)
        val pathImage = "C:\\Users\\regha\\Рабочий стол\\2\\2.jpg"
        val list = mutableListOf<IImage>()
        list.add(doc.insertImage(pathImage, 150, 150, 0))
        list.add(doc.insertImage(pathImage, 150, 150, 1))
        list.add(doc.insertImage(pathImage, 150, 150, 2))
        doc.undo()
        doc.undo()
        doc.undo()
        list.add(doc.insertImage(pathImage, 150, 150, 0))

        assertEquals(File(list[0].getString()).exists(), false)
        assertEquals(File(list[1].getString()).exists(), false)
        assertEquals(File(list[2].getString()).exists(), false)
        assertEquals(File(list[3].getString()).exists(), true)
    }

}