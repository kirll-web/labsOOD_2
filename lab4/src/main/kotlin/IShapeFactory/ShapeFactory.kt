package IShapeFactory

import Color.ShapeColor
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import common.*
import shape.*

//command : AddShape Circle #000000 (cx)0.0 (cy)0.0 (rx)0.0 (ry)0.0
class ShapeFactory: IShapeFactory {
    override fun createShape(descr: String): Shape? = parseCommand(descr)

    private fun parseCommand(command: String): Shape? {
        val args = command.trim().mySplit()

        if (args.isArgsNotEnough(2)) return null

        val headCommand = parseCommandHead(args[0])

        return when (headCommand) {
            Commands.ADD_SHAPE -> {
                get(args.slice(1 until args.size))
            }

            Commands.UNKNOWN -> {
                println("unknown command")
                null
            }
        }
    }

    enum class Commands {
        ADD_SHAPE,
        UNKNOWN
    }

    private fun parseCommandHead(head: String) = when (head) {
        "AddShape" -> Commands.ADD_SHAPE
        else -> Commands.UNKNOWN
    }

    private fun parseViewShape(viewShape: String): ViewShape = when (viewShape) {
        "rectangle" -> ViewShape.RECTANGLE
        "regular_polygon" -> ViewShape.REGULAR_POLYGON
        "ellipse" -> ViewShape.ELLIPSE
        "triangle" -> ViewShape.TRIANGLE
        else -> ViewShape.UNKNOWN
    }

    enum class ViewShape {
        RECTANGLE,
        REGULAR_POLYGON,
        ELLIPSE,
        TRIANGLE,
        UNKNOWN
    }

    private fun get(args: List<String>): Shape? {
        if (args.isArgsNotEnough(3)) return null
        val shapeType = parseViewShape(args[0])

        val color = when (val result = args[1].getShapeColor()) {
            null -> return null
            else -> result
        }

        val newArgs = args.slice(2 until args.size)

        return when (shapeType) {
            ViewShape.RECTANGLE -> getRectangle(newArgs, color)

            ViewShape.ELLIPSE -> getEllipse(newArgs, color)

            ViewShape.TRIANGLE -> getTriangle(newArgs, color)

            ViewShape.REGULAR_POLYGON -> getRegularPolygon(newArgs, color)

            else -> {
                println("unknown shape")
                null
            }
        }
    }

    private fun getRectangle(
        args: List<String>,
        color: ShapeColor
    ): Rectangle? {
        if (args.isArgsNotEnough(4)) return null
        //AddShape rectangle #ff0000 <left> <top> <right> <bottom>
        val left = args[0]
        val top = args[1]
        val right = args[2]
        val bottom = args[3]

        if (!(left.isDouble("left")
                    && top.isDouble("top")
                    && right.isDouble("right")
                    && bottom.isDouble("bottom"))
        ) return null


        return Rectangle(
            leftTop = (left to top).convertDoubleToPoint(),
            rightBottom = (right to bottom).convertDoubleToPoint(),
            color
        )
    }

    private fun getEllipse(
        args: List<String>,
        color: ShapeColor
    ): Ellipse? {
        // AddShape Circle #000000 (cx)0.0 (cy)0.0 (rx)0.0 (ry)0.0
        if (args.isArgsNotEnough(4)) return null
        val cx = args[0]
        val cy = args[1]
        val rx = args[2]
        val ry = args[3]

        if (
            !(cx.isDouble("x")
                    && cy.isDouble("y")
                    && rx.isDouble("horizontal radius")
                    && ry.isDouble("vertical radius"))
        ) return null

        return Ellipse(
            center = (cx to cy).convertDoubleToPoint(),
            horizontalRadius = rx.toDouble(),
            verticalRadius = ry.toDouble(),
            color = color
        )
    }

    private fun getTriangle(
        args: List<String>,
        color: ShapeColor
    ): Triangle? {
        if (args.isArgsNotEnough(6)) return null
        //AddShape sh1 #ff0000 triangle <x1> <y1> <x2> <y2> <x3> <y3>
        val x1 = args[0]
        val y1 = args[1]
        val x2 = args[2]
        val y2 = args[3]
        val x3 = args[4]
        val y3 = args[5]

        if (!(x1.isDouble("x1")
                    && y1.isDouble("y1")
                    && x2.isDouble("x2")
                    && y2.isDouble("y2")
                    && x3.isDouble("x3")
                    && y3.isDouble("y3"))
        ) return null

        return Triangle(
            v1 = (x1 to y1).convertDoubleToPoint(),
            v2 = (x2 to y2).convertDoubleToPoint(),
            v3 = (x3 to y3).convertDoubleToPoint(),
            color
        )
    }

    private fun getRegularPolygon(
        args: List<String>,
        color: ShapeColor
    ): RegularPolygon? {
        if (args.isArgsNotEnough(4)) return null

        val cx = args[0]
        val cy = args[1]
        val vertexCount = args[2]
        val radius = args[3]

        if (
            !(cx.isDouble("cx")
                    && cy.isDouble("cy")
                    && vertexCount.isUInt("vertex count")
                    && radius.isDouble("radius"))
        ) return null

        return RegularPolygon(
            center = (cx to cy).convertDoubleToPoint(),
            vertexCount = vertexCount.toUInt(),
            radius = radius.toDouble(),
            color = color
        )
    }

    private fun <T> List<T>.isArgsNotEnough(count: Int) = when {
        size < count ->  {
            println("The arguments is not enough")
            true
        }
        else -> false
    }

    private fun String.getShapeColor() = when (this.toLowerCase(Locale.current)) {
        "pink" -> ShapeColor.PINK
        "yellow" -> ShapeColor.YELLOW
        "green" -> ShapeColor.GREEN
        "black" -> ShapeColor.BLACK
        "red" -> ShapeColor.RED
        "blue" -> ShapeColor.BLUE
        else -> {
            println("This color is not exists")
            null
        }
    }

}