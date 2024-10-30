package org.z1

import java.io.File


class Core {

    companion object {
        const val MAX_LENGTH_TEMP_NAME = 12;

        fun generateRandomFileName(fileNameLength: Int) : String{
            val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')

            return (1 ..fileNameLength).map { allowedChars.random() }.joinToString("")
        }
    }

}

private fun createFolder()  {
    val directory = File(Core.generateRandomFileName(Core.MAX_LENGTH_TEMP_NAME))
    if (directory.exists()) {
        directory.delete()
    }

    directory.mkdirs()
}
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    createFolder()
}