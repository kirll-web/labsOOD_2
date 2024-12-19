package org.command

import CommandHandler
import Menu.Menu
import Menu.MenuItem
import MultiGumballMachine.MultiGumballMachine


fun main() {
    val startGumball = 5u
    val machine = MultiGumballMachine(startGumball)

    val menu = Menu()

    val commandHandler = CommandHandler(menu, machine)

    commandHandler.run()
}