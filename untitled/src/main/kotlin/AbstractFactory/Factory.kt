package org.command.AbstractFactory

interface Factory {
    fun createCouch(): Couch
    fun createArmchair(): Armchair
}

class VictorianFurnitureFactory(): Factory {
    override fun createCouch() = VictorianCouch()

    override fun createArmchair() = VictorianArmchair()
}

class ModernFurnitureFactory(): Factory {
    override fun createCouch() = ModernCouch()

    override fun createArmchair() = ModernArmchair()
}