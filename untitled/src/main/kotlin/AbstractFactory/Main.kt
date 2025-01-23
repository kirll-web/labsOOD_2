package org.command.AbstractFactory

fun main() {
    val factories: List<Factory> = listOf(
        VictorianFurnitureFactory(),
        ModernFurnitureFactory()
    )

    factories.forEach { factory ->
        val couch = factory.createCouch()
        val armchair = factory.createArmchair()

        couch.print()
        armchair.print()
    }
}