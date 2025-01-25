package org.command.Builder

/** "Product"  */
internal class Pizza {
    private var dough = ""
    private var sauce = ""
    private var topping = ""

    fun setDough(dough: String) {
        this.dough = dough
    }

    fun setSauce(sauce: String) {
        this.sauce = sauce
    }

    fun setTopping(topping: String) {
        this.topping = topping
    }
}


/** "Abstract Builder"  */
abstract class PizzaBuilder {
    var pizza: Pizza? = null
        protected set

    fun createNewPizzaProduct() {
        pizza = Pizza()
    }

    abstract fun buildDough()
    abstract fun buildSauce()
    abstract fun buildTopping()
}

class HawaiianPizzaBuilder : PizzaBuilder() {
    override fun buildDough() {
        pizza!!.setDough("cross")
    }

    override fun buildSauce() {
        pizza!!.setSauce("mild")
    }

    override fun buildTopping() {
        pizza!!.setTopping("ham+pineapple")
    }
}

class SpicyPizzaBuilder : PizzaBuilder() {
    override fun buildDough() {
        pizza!!.setDough("pan baked")
    }

    override fun buildSauce() {
        pizza!!.setSauce("hot")
    }

    override fun buildTopping() {
        pizza!!.setTopping("pepperoni+salami")
    }
}


class Waiter {
    private var pizzaBuilder: PizzaBuilder? = null

    fun setPizzaBuilder(pb: PizzaBuilder?) {
        pizzaBuilder = pb
    }

    val pizza: Pizza?
        get() = pizzaBuilder!!.pizza

    fun constructPizza() {
        pizzaBuilder!!.createNewPizzaProduct()
        pizzaBuilder!!.buildDough()
        pizzaBuilder!!.buildSauce()
        pizzaBuilder!!.buildTopping()
    }
}

class PizzaStore(
    private val pizzaBuilder: 
) {

}


fun main(args: Array<String>) {
    val waiter = Waiter()
    val hawaiianPizzaBuilder: PizzaBuilder = HawaiianPizzaBuilder()
    waiter.setPizzaBuilder(hawaiianPizzaBuilder)
    waiter.constructPizza()

    val pizza = waiter.pizza
}