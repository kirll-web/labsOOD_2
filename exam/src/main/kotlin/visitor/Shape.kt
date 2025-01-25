package org.command.visitor

abstract class Shape {
    abstract fun accept(visitor: IVisitor<Shape>)
}

class Rectangle(
    val width: Int,
    val height: Int
): Shape() {
    override fun accept(visitor: IVisitor<Shape>) {
        visitor.visit(this)
    }
}

class Circle(
    val radius: Int
): Shape() {
    override fun accept(visitor: IVisitor<Shape>) {
        visitor.visit(this)
    }
}