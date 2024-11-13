package org.command.Menu


typealias FPCommand = (input: String) -> Unit

data class MenuItem(
    val shortcut: String,
    val descr: String,
    val command: FPCommand
)

class Menu {
    private var mMenuItems = mutableMapOf(
        "exit" to MenuItem("exit", "close programm") { isCanExit = true }
    )
    private var isCanExit = false

    fun addItem(item: MenuItem) {
        mMenuItems[item.shortcut] = item
    }

    fun run() {
        while (!isCanExit) {
            executeCommand(readln())
        }
    }

    fun showMenu() {
        println("Command list:")
        mMenuItems.forEach {
            println("${it.value.shortcut}: ${it.value.descr}")
        }
    }

    private fun executeCommand(commandLine: String) {
        val args = commandLine.trim().split(" ")
        if (args.isEmpty()) {
            println("Incorrect  input")
            return
        }

        val headCommand = args[0]
        val menuItem = mMenuItems[headCommand]

        when {
            menuItem != null -> menuItem.command(
                when {
                    args.isNotEmpty() ->
                        args
                            .slice(1 until args.size)
                            .joinToString(" ")

                    else -> ""
                }
            )

            else -> {
                println("Unknown command")
            }
        }
    }
}