import org.command.Adapter.ModernGraphicsAdapter
import org.command.Adapter.ModernGraphicsClassAdapter
import org.command.modern_graphics_lib.ModernGraphicsRenderer
import org.command.modern_graphics_lib.RGBAColor
import org.command.shape_drawing_lib.CanvasPainter
import org.command.shape_drawing_lib.Point
import org.command.shape_drawing_lib.Triangle
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals
import org.command.modern_graphics_lib.Point as ModernPoint

class Test {

    @Test
    fun testAdapterObject() {
        val p1 =  Point(10, 15)
        val p2 =  Point(100, 300)
        val p3 =  Point(150, 250)

        val color = 0x3F3F3Fu
        val triangle = Triangle(
            p1,
            p2,
            p3,
            color
        )

        val strm1 = ByteArrayOutputStream()
        val renderer1 = ModernGraphicsRenderer(strm1)
        renderer1.beginDraw()
        renderer1.drawLine(pointToModernPoint(p1), pointToModernPoint(p2), colorToRGBA(color))
        renderer1.drawLine(pointToModernPoint(p2),  pointToModernPoint(p3),  colorToRGBA(color))
        renderer1.drawLine(pointToModernPoint(p3),  pointToModernPoint(p1),  colorToRGBA(color))
        renderer1.endDraw()

        val strm2 = ByteArrayOutputStream()
        val renderer2 = ModernGraphicsRenderer(strm2)
        renderer2.beginDraw()
        val adapter = ModernGraphicsAdapter(renderer2)
        val painter = CanvasPainter(adapter)
        painter.draw(triangle)
        adapter.close()
        assertEquals(strm1.toString(), strm2.toString())
    }

    @Test
    fun testAdapterClass() {
        val p1 =  Point(10, 15)
        val p2 =  Point(100, 300)
        val p3 =  Point(150, 250)

        val color = 0x3F3F3Fu
        val triangle = Triangle(
            p1,
            p2,
            p3,
            color
        )

        val strm1 = ByteArrayOutputStream()
        val renderer1 = ModernGraphicsRenderer(strm1)
        renderer1.beginDraw()
        renderer1.drawLine(pointToModernPoint(p1), pointToModernPoint(p2), colorToRGBA(color))
        renderer1.drawLine(pointToModernPoint(p2),  pointToModernPoint(p3),  colorToRGBA(color))
        renderer1.drawLine(pointToModernPoint(p3),  pointToModernPoint(p1),  colorToRGBA(color))
        renderer1.endDraw()

        val strm2 = ByteArrayOutputStream()
        val adapter = ModernGraphicsClassAdapter(strm2)
        adapter.beginDraw()
        val painter = CanvasPainter(adapter)
        painter.draw(triangle)
        adapter.endDraw()
        assertEquals(strm1.toString(), strm2.toString())
    }



    private fun colorToRGBA(rgbColor: UInt) = RGBAColor(
        r = (rgbColor shr 16 and 0xFFu).toFloat() / 255.0f,
        g = (rgbColor shr 8 and 0xFFu).toFloat() / 255.0f,
        b = (rgbColor and 0xFFu).toFloat() / 255.0f,
        a = 1.0F
    )

    private fun pointToModernPoint(p: Point) = ModernPoint(p.x, p.y)

}