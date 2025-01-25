interface Prototype {
    fun clone(): Prototype
}

class Bike(
    private var gears: Int,
    private var bikeType: String
) : Prototype {
    var year: Int = 2005
        private set

    constructor(other: Bike) : this(other.gears, other.bikeType) {
        this.year = other.year
    }

    override fun clone(): Prototype {
        val prototype = Bike(this)
        return prototype
    }

    fun makeAdvanced() {
        bikeType = "Advanced"
        gears = 6
        year = 2009
    }
}

fun makeJaguar(basicBike: Bike): Bike {
    basicBike.makeAdvanced()
    return basicBike
}

fun main() {
    val bike = Bike(3, "Yamaha")
    val basicBike = bike.clone()
    val advancedBike = makeJaguar(basicBike as Bike)
    println("bike: " + bike.year)
    println("Prototype Design Pattern: " + advancedBike.year)
}