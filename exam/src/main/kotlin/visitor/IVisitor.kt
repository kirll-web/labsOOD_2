package org.command.visitor


interface IVisitor<T> {
    fun visit(shape: T)
}

class XMLShapeVisitor() : IVisitor<Shape>  {
    override fun visit(shape: Shape) {
        when (shape) {
            is Rectangle -> {
                println("<Rectangle \n width=${shape.width} \n height=${shape.height}\n />")
            }
            is Circle -> {
                println("<Circle \n radius=${shape.radius}\n")
            }
        }
    }
}

class ConsoleShapeVisitor() : IVisitor<Shape>  {
    override fun visit(shape: Shape) {
        when (shape) {
            is Rectangle -> {
                println("Rectangle, width: ${shape.width}, height: ${shape.height}/>")
            }
            is Circle -> {
                println("Circle radius: ${shape.radius}")
            }
        }
    }
}