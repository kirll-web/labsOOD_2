package Canvas

import Color.ShapeColor
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer

class Line(
    private val start: Offset,
    private val end: Offset,
    private val color: ShapeColor
): IPrimitive {
    override fun draw(drawScope: DrawScope, rememberText: TextMeasurer) {
        drawScope.drawLine(
            color = color.parseToComposeColor(),
            start = start,
            end = end
        )
    }
}