import Canvas.RectD
import common.Point
import org.junit.jupiter.api.Test
import shape.Figures.Ellipse
import shape.Figures.Rectangle
import shape.Figures.Triangle
import shape.Figures.TrianglePoints
import shape.ShapeGroup
import shape.Styles.FillStyle
import shape.Styles.StrokeStyle
import kotlin.test.assertEquals

fun templateForTestFrameSize(): ShapeGroup {
    val group = ShapeGroup().apply {
        insertShape(
            Rectangle(
                rect = RectD(
                    0.0, 0.0, 200.0, 200.0
                ),
                fillStyle = FillStyle(0xFFdf7a0f),
                strokeStyle = StrokeStyle(0u,null )
            ), 0u
        )
        insertShape(
            Rectangle(
                rect = RectD(
                    200.0, 200.0, 300.0, 400.0
                ),
                fillStyle = FillStyle(0xFFdf7a0f),
                strokeStyle = StrokeStyle(0u,null )
            ), 1u
        )
    }

    return group
}

class Test {

    @Test
    fun testFrameSizeOfRectangle() {
        val rectangle =  Rectangle(
            rect = RectD(
                0.0, 400.0, 800.0, 200.0
            ),
            fillStyle = FillStyle(0xFF19a141),
            strokeStyle = StrokeStyle(0u,null )
        )
        val frame = rectangle.getFrameImpl()
        assertEquals(frame.left, 0.0)
        assertEquals(frame.top, 400.0)
        assertEquals(frame.width, 800.0)
        assertEquals(frame.height, 200.0)
    }

    @Test
    fun testFrameSizeOfEllipse() {
        val ellipse = Ellipse(
            rect = RectD(
                0.0, 400.0, 800.0, 200.0
            ),
            fillStyle = FillStyle(0xFF19a141),
            strokeStyle = StrokeStyle(0u,null )
        )
        val frame = ellipse.getFrameImpl()
        assertEquals(frame.left, 0.0)
        assertEquals(frame.top, 400.0)
        assertEquals(frame.width, 800.0)
        assertEquals(frame.height, 200.0)
    }

    @Test
    fun testFrameSizeOfTriangle() {
        val height = 100.0
        val width = 200.0
        val triangle = Triangle(
            TrianglePoints(
                Point(0.0, 0.0),
                Point(0.0, height),
                Point(width, 0.0)
            ),
            fillStyle = FillStyle(0xFF19a141),
            strokeStyle = StrokeStyle(2u, 0xFF000000 )
        )
        val frame = triangle.getFrameImpl()
        assertEquals(frame.left, 0.0)
        assertEquals(frame.top, 0.0)
        assertEquals(frame.width, 200.0)
        assertEquals(frame.height, 100.0)
    }

    @Test
    fun testStrokeStyleRectangle() {
        val color = 0xFF19a141
        val width = 2u
        val rectangle =  Rectangle(
            rect = RectD(
                0.0, 400.0, 800.0, 200.0
            ),
            fillStyle = FillStyle(0xFF19a141),
            strokeStyle = StrokeStyle(width,color)
        )
        val strokeStyle = rectangle.getStrokeStyle()
        assertEquals(strokeStyle.getColor(), color)
        assertEquals(strokeStyle.getWidth(), width)
    }

    @Test
    fun testStrokeStyleEllipse() {
        val color = 0xFF19a141
        val width = 2u
        val ellipse = Ellipse(
            rect = RectD(
                0.0, 400.0, 800.0, 200.0
            ),
            fillStyle = FillStyle(0xFF19a141),
            strokeStyle = StrokeStyle(width,color )
        )
        val strokeStyle = ellipse.getStrokeStyle()
        assertEquals(strokeStyle.getColor(), color)
        assertEquals(strokeStyle.getWidth(), width)
    }

    @Test
    fun testStrokeStyleTriangle() {
        val color = 0xFF19a141
        val width = 2u
        val triangle = Triangle(
            TrianglePoints(
                Point(0.0, 0.0),
                Point(0.0, 100.0),
                Point(200.0, 0.0)
            ),
            fillStyle = FillStyle(0xFF19a141),
            strokeStyle = StrokeStyle(2u,color )
        )
        val strokeStyle = triangle.getStrokeStyle()
        assertEquals(strokeStyle.getColor(), color)
        assertEquals(strokeStyle.getWidth(), width)
    }

    @Test
    fun testFrameSizeOfGroupShape() {
        val exceptedLeft = 0.0
        val exceptedTop = 0.0
        val exceptedWidth = 500.0
        val exceptedHeight = 600.0
        val group = templateForTestFrameSize()

        val frame = group.getFrame()
        assertEquals(frame?.left, exceptedLeft)
        assertEquals(frame?.top, exceptedTop)
        assertEquals(frame?.width, exceptedWidth)
        assertEquals(frame?.height, exceptedHeight)
    }


    @Test
    fun testResizeFrameOfGroupShape() {
        val exceptedLeft = 300.0
        val exceptedTop = 300.0
        val exceptedWidth = 700.0
        val exceptedHeight = 1000.0
        val group = templateForTestFrameSize()

        group.setFrame(RectD(exceptedLeft, exceptedTop, exceptedWidth, exceptedHeight))

        val frame2 = group.getFrame()
        assertEquals(frame2?.left, exceptedLeft)
        assertEquals(frame2?.top, exceptedTop)
        assertEquals(frame2?.width, exceptedWidth)
        assertEquals(frame2?.height, exceptedHeight)
    }

    @Test
    fun testStrokeStyleGroupShape() {
        val group = ShapeGroup().apply {
            insertShape(
                Rectangle(
                    rect = RectD(
                        0.0, 0.0, 200.0, 200.0
                    ),
                    fillStyle = FillStyle(0xFFdf7a0f),
                    strokeStyle = StrokeStyle(2u,0xFF00000)
                ), 0u
            )
            insertShape(
                Rectangle(
                    rect = RectD(
                        200.0, 200.0, 300.0, 400.0
                    ),
                    fillStyle = FillStyle(0xFFdf7a0f),
                    strokeStyle = StrokeStyle(2u,0xFF31a52b)
                ), 1u
            )
            insertShape(
                Rectangle(
                    rect = RectD(
                        200.0, 200.0, 300.0, 400.0
                    ),
                    fillStyle = FillStyle(0xFFdf7a0f),
                    strokeStyle = StrokeStyle(2u,0xFF00000)
                ), 1u
            )
        }

        val exceptedColor = 0xFFdf7a0f
        val exceptedWidth = 2u
        val style = group.getStrokeStyle()
        assertEquals(style.getColor(), null)
        assertEquals(style.getWidth(), 0u)

        group.getStrokeStyle().setColor(exceptedColor)
        group.getStrokeStyle().setWidth(exceptedWidth)

        val style2 = group.getStrokeStyle()
        assertEquals(style2.getColor(), exceptedColor)
        assertEquals(style2.getWidth(), exceptedWidth)
    }

    @Test
    fun testFillStyleGroupShape() {
        val group = ShapeGroup().apply {
            insertShape(
                Rectangle(
                    rect = RectD(
                        0.0, 0.0, 200.0, 200.0
                    ),
                    fillStyle = FillStyle(0xFFdf7a0f),
                    strokeStyle = StrokeStyle(2u,0xFF00000)
                ), 0u
            )
            insertShape(
                Rectangle(
                    rect = RectD(
                        200.0, 200.0, 300.0, 400.0
                    ),
                    fillStyle = FillStyle(0xFF31a52b),
                    strokeStyle = StrokeStyle(2u,0xFF30a52b)
                ), 1u
            )
            insertShape(
                Rectangle(
                    rect = RectD(
                        200.0, 200.0, 300.0, 400.0
                    ),
                    fillStyle = FillStyle(0xFFdf7a0f),
                    strokeStyle = StrokeStyle(2u,0xFF00000)
                ), 1u
            )
        }

        val exceptedColor = 0xFFdf7a0f
        val style = group.getFillStyle()
        assertEquals(style.getColor(), null)

        group.getFillStyle().setColor(exceptedColor)

        val style2 = group.getFillStyle()
        assertEquals(style2.getColor(), exceptedColor)
    }
}