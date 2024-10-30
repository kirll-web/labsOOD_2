package Canvas

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer

interface IPrimitive {
    fun draw(drawScope: DrawScope, rememberText: TextMeasurer)
}