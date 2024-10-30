package Canvas

import Color.ShapeColor
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.TextMeasurer

class Ellipse(
    private val topLeft: Offset,
    private val rx: Double,
    private val ry: Double,
    private val color: ShapeColor
): IPrimitive {
    override fun draw(drawScope: DrawScope, rememberText: TextMeasurer) {
        drawScope.drawOval(
            color = color.parseToComposeColor(),
            topLeft = topLeft,
            size = Size(rx.toFloat(), ry.toFloat()),
            style = Fill
        )
    }
}