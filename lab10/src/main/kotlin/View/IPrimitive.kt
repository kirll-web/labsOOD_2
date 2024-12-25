package View

import RGBAColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.jetbrains.skia.Image as SkiaImage
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import java.io.File

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

    data class Image(
        private val topLeft: Offset,
        private val width: Float,
        private val height: Float,
        val imageBitmap: ImageBitmap,
    ): Primitive {
        private fun loadImageFromFile(filePath: String): ImageBitmap? {
            return try {
                val skiaImage = SkiaImage.makeFromEncoded(File(filePath).readBytes())
                skiaImage.toComposeImageBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }


        fun draw(drawScope: DrawScope?) {
            drawScope?.drawImage(
                image =  imageBitmap,
                srcOffset = IntOffset.Zero,
                srcSize =  IntSize(imageBitmap.width, imageBitmap.height),
                dstOffset = IntOffset(topLeft.x.toInt(), topLeft.y.toInt()),
                dstSize = IntSize(width.toInt(), height.toInt()),
            )
        }
    }
}