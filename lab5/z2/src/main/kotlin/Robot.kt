package org.command

enum class WalkDirection {
    North,
    South,
    West,
    East,
}

class Robot {
    private var mTurnedOn = false
    private var mDirection: WalkDirection? = null

    fun turnOn() {
        if (!mTurnedOn) {
            mTurnedOn = true
            println("It am waiting for your commands")
        }
    }

    fun turnOff() {
        if (mTurnedOn) {
            mTurnedOn = false
            mDirection = null
            println("It is a pleasure to serve you")
        }
    }

    fun walk(direction: WalkDirection) {

        if (mTurnedOn) {
            mDirection = direction
            val directionToString = mapOf(
                WalkDirection.East to "east",
                WalkDirection.South to "south",
                WalkDirection.West to "west",
                WalkDirection.North to "north",
            )

            println("Walking ${directionToString[direction]}")
        } else {
            println("The robot should be turned on first")
        }
    }

    fun stop() {
        if (mTurnedOn) {
            if (mDirection != null) {
                mDirection = null
                println("Stopped")
            } else {
                println("I am staying still")
            }
        } else {
            println("The robot should be turned on first")
        }
    }

}