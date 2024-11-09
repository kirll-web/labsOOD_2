package org.command.Ext

import org.command.Document.IDocument
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.exists

fun String.isInt(): Boolean = try {
    this.toInt()
    true
} catch (ex: Exception) {
    false
}

fun List<String>.isListInt(): Boolean {
    this.forEach {
        if (!it.isInt()) return false
    }
    return true
}

const val MAX_LENGTH_TEMP_NAME = 12

fun generateRandomFileName(fileNameLength: Int): String {
    val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    return (1..fileNameLength).map { allowedChars.random() }.joinToString("")
}

fun encodeHtml(str: String): String {
    val symbols = mapOf(
        '&' to "&amp",
        '\\' to "&quot",
        '\'' to "&apos",
        '<' to "&lt",
        '>' to "&gt"
    )

    var newStr = ""

    str.forEach { ch ->
        newStr += symbols[ch] ?: ch
    }

    return newStr
}


fun saveToHtml(document: IDocument, file: File, imageFolder: Path) {

    if (!file.exists()) {
        file.createNewFile()
    }

    var str = ""

    str += "<!DOCTYPE html><html><head></head><body>"
    if (document.getTitle().isNotBlank()) {
        str += "<h1>${document.getTitle()}</h1>"
    }

    for (i in 0 until document.getItemsCount())
    {
        val item = document.getItem(i)

        if (item.getParagraph() != null)
        {
            val paragraph = item.getParagraph()
            str += "<p>${paragraph?.getText()?.let { encodeHtml(it) }}</p>"
        }
        else if (item.getImage() != null)
        {
            val image = item.getImage()

            val itemImagePath = image?.getString()
            val docImagePath = Path(
                "$imageFolder\\${Path(itemImagePath!!).fileName}"
            )
            itemImagePath.let {
                Files.copy(Path(itemImagePath), docImagePath, StandardCopyOption.REPLACE_EXISTING)
            }

            str += "<img src=\"${docImagePath}\" width=\"${image?.getWidth()}px\" height=\"${image?.getHeight()}px\""
        }
    }

    str += "</body></html>"
    file.writeText(str)
}