package org.command.AbstractFactory

interface Furniture {
    fun print()
}
interface Couch: Furniture
interface Armchair: Furniture

class VictorianArmchair(): Armchair {
    override fun print() {
        println("I'm Victorian couch")
    }
}
class ModernArmchair(): Armchair {
    override fun print() {
        println("I'm Modern couch")
    }
}

class VictorianCouch(): Couch {
    override fun print() {
        println("I'm Victorian couch")
    }
}
class ModernCouch(): Couch {
    override fun print() {
        println("I'm Modern couch")
    }
}

