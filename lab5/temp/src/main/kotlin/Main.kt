package org.z1

import java.awt.Point
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.createTempDirectory


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val file = File( "C:\\Users\\regha\\Рабочий стол\\2.jpg")
    val fileName = file.nameWithoutExtension
    val extension = file.extension
    val to = Path("C:\\Users\\regha\\Рабочий стол\\2\\${fileName}.${extension}")
    Files.copy(Path(file.path), to, StandardCopyOption.REPLACE_EXISTING)
}