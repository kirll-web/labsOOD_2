package View

import RGBAColor
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke

sealed interface Primitive{
    class Ellipse(
        private val topLeft: Offset,
        private val rx: Int,
        private val ry: Int,
    ): Primitive {
        fun draw(drawScope: DrawScope, fillColor: RGBAColor?, strokeWidth: UInt, strokeColor: RGBAColor?) {
            fillColor?.let { color ->
                drawScope.drawOval(
                    color = Color(color),
                    topLeft = topLeft,
                    size = Size(rx.toFloat(), ry.toFloat()),
                    style = Fill
                )
            }
            strokeColor?.let { color ->
                drawScope.drawOval(
                    color = Color(color),
                    topLeft = topLeft,
                    size = Size(rx.toFloat(), ry.toFloat()),
                    style = Stroke(width = strokeWidth.toFloat())
                )
            }
        }
    }

    class Line(
        private val start: Offset,
        private val end: Offset
    ): Primitive {
        fun draw(path: Path) = path.apply {
            if(path.isEmpty) moveTo(start.x, start.y)
            lineTo(end.x, end.y)
        }
    }

    data class Fill(
        val color: RGBAColor?
    ): Primitive

    data class Stroke(
        val color: RGBAColor?,
        val strokeWidth: UInt
    ): Primitive
}