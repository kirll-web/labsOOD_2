import Designer.Designer
import IShapeFactory.ShapeFactory
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import org.junit.jupiter.api.Test
import org.w3c.dom.css.CSSImportRule
import shape.Ellipse
import shape.Rectangle
import shape.RegularPolygon
import shape.Triangle

class Test {
    @Test
    fun testCreateDraftDesigner() {
        val shapeFactory = ShapeFactory()
        val designer = Designer(shapeFactory)

        val draft = designer.mockCreateDraft(listOf(
            "AddShape rectangle black 0 0 100 100",
            "AddShape circle black 0 0 100 100",
            "exit"
        ))

        assertEquals(draft.getShapeCount(),1)
        assertNotEquals(draft.getShapeCount(),2)
    }
    @Test
    fun testCreateRectangle() {
        val shapeFactory = ShapeFactory()

        val shape1 = shapeFactory.createShape( "AddShape rectangle black 0 0 100 100")
        val shape2 = shapeFactory.createShape( "AddShape rectangle black ")

        val result1 = shape1 is Rectangle
        val result2 = shape2 == null
        assertEquals(result1, true)
        assertNotEquals(result2, false)
    }
    @Test
    fun testCreateEllipse() {
        val shapeFactory = ShapeFactory()

        val shape1 = shapeFactory.createShape( "AddShape ellipse black 0 0 50 100")
        val shape2 = shapeFactory.createShape( "AddShape ellipse black 0 0")
        val result1 = shape1 is Ellipse
        val result2 = shape2 == null
        assertEquals(result1, true)
        assertNotEquals(result2, false)
    }
    @Test
    fun testCreateRegularPolygon() {
        val shapeFactory = ShapeFactory()

        val shape1 = shapeFactory.createShape( "AddShape regular_polygon black 0 0 50 5")
        val shape2 = shapeFactory.createShape( "AddShape regular_polygon black 0 0 50")
        val result1 = shape1 is RegularPolygon
        val result2 = shape2 == null
        assertEquals(result1, true)
        assertNotEquals(result2, false)
    }
    @Test
    fun testCreateTriangle() {
        val shapeFactory = ShapeFactory()

        val shape1 = shapeFactory.createShape( "AddShape triangle black 0 0 50 50 0 50")
        val shape2 = shapeFactory.createShape( "AddShape triangle 0 0 50 50 0")
        val result1 = shape1 is Triangle
        val result2 = shape2 == null
        assertEquals(result1, true)
        assertNotEquals(result2, false)
    }
    @Test
    fun testInputIncorrectColor() {
        val shapeFactory = ShapeFactory()
        val shape = shapeFactory.createShape( "AddShape triangle purple 0 0 50 50 0 50")
        assertEquals(shape, null)
    }
}