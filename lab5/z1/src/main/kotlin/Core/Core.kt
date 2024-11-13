package Core

import Document.IDocument
import java.io.File
import java.nio.file.*
import kotlin.io.path.Path
import kotlin.io.path.exists

typealias FPCommand = (input: String) -> Unit

class Core {




    companion object {
        const val MAX_LENGTH_TEMP_NAME = 12

        fun encodeHtml(str: String): String {
            val symbols = mapOf(
                "&" to "&amp",
                "\\" to "&quot",
                "\'" to "&apos",
                "<" to "&lt",
                ">" to "&gt"
            )

            symbols.forEach { symbol ->
                str.replace(symbol.key, symbol.value)
            }

            return str
        }

        fun generateRandomFileName(fileNameLength: Int): String {
            val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')

            return (1..fileNameLength).map { allowedChars.random() }.joinToString("")
        }

        fun saveToHtml(document: IDocument, file: File, imageFolder: Path) {
            file.writeText("<!DOCTYPE html><html><head></head><body>")
            if (document.getTitle().isNotBlank()) {
                file.writeText("<h1>${document.getTitle()}</h1>")
            }

            for (i in 0 .. document.getItemsCount())
            {
                val item = document.getItem(i)

                if (item?.getParagraph() != null)
                {
                    val paragraph = item.getParagraph()
                    file.writeText("<p>${paragraph?.getText()?.let { encodeHtml(it) }}</p>")
                }
                else if (item?.getImage() != null)
                {
                    if (!imageFolder.exists())
                    {
                        File(generateRandomFileName(MAX_LENGTH_TEMP_NAME))
                    }

                    val image = item.getImage()

                    val itemImagePath = image?.getString()
                    val docImagePath = Path(
                        (imageFolder + "/" + itemImagePath?.fileName).toString()
                    )
                    itemImagePath?.let {
                        Files.copy(it, docImagePath, StandardCopyOption.REPLACE_EXISTING)
                    }

                    file.writeText(
                        "<img src=\"${imageFolder.fileName}/${docImagePath.fileName}\"" +
                                "width=\"${image?.getWidth()}\"px" +
                                "height=\"${image?.getHeight()}\"px"
                    )
                }
            }

            file.writeText("</body></html>")
        }
    }

}