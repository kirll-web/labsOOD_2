package Color

import androidx.compose.ui.graphics.Color as ComposeColor
import common.fromHex

enum class ShapeColor(private val value: String) {
    GREEN("#25ff00"),
    RED("#ff0000"),
    BLUE("#07d2f7"),
    YELLOW("#fbeb03"),
    PINK("#ff07e1"),
    BLACK("#000000");

    fun parseToComposeColor() = ComposeColor.fromHex(value)
}
