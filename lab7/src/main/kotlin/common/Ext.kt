package common

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import java.util.regex.Pattern


fun Color.Companion.fromHex(colorString: String) =
    Color(colorString.removePrefix("#").toLong(16) or 0x00000000FF000000)


fun String.isDouble(name: String) = try {
    this.toDouble()
    true
} catch (ex: Exception) {
    println("$name is not double number")
    false
}

fun String.mySplit() = this.split(" ")

fun getOffset(x: Double, y: Double) = Offset(x = x.toFloat(), y = y.toFloat())

fun String.isHexColorCode() = Pattern.matches("#[A-Fa-f0-9]{6}", this)


fun List<String>.isEnoughArgs(minElems: Int) = when {
    this.size >= minElems -> true
    else -> {
        println("Arguments is not enough")
        false
    }
}

fun Pair<String, String>.convertDoubleToPoint() = Point(this.first.toDouble(), this.second.toDouble())
