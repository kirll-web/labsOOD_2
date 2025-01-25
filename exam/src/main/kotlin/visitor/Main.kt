package org.command.visitor


fun main() {
    val xmlVisitor = XMLShapeVisitor()
    val consoleVisitor = ConsoleShapeVisitor()
    val list: List<Shape> = listOf(
        Rectangle(10, 20),
        Circle(50),
        Rectangle(30, 40),
        Circle(100)
    )

    list.forEach {
        it.accept(xmlVisitor)
        it.accept(consoleVisitor)
    }
}