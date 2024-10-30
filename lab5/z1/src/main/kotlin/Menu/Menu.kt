package Menu

import Core.FPCommand
import java.util.*
import javax.sound.sampled.Line

data class MenuItem(
    val name: String,
    val description: String,
    val command: FPCommand
)

class Menu {
    private var mMenuItems = listOf<MenuItem>()
    private var mCanExit = false
    fun addItem(name: String, description: String, command: FPCommand) {
        mMenuItems = mMenuItems.plus(
            MenuItem(
                name = name,
                description = description,
                command = command
            )
        )
    }

    fun run() {
        showMenu()

        while (!mCanExit) {
            val command = readln()
            executeCommand(command)
        }
    }

    fun showMenu() {
        println("Command list:")
        mMenuItems.forEach {
            println("${it.name}: ${it.description}")
        }
    }

    fun exit() {}

    private fun executeCommand(commandLine: String) {
        val args = commandLine.trim().split(" ")
        if(args.size == 0) {
            println("Incorrect  input")
            return
        }

        val headCommand = args[0]
        val menuItem = mMenuItems.find {
            it.name == headCommand
        }

        when {
            menuItem != null -> menuItem.command(
                when {
                    args.size >= 1 ->
                        args.slice(1 until args.size)
                            .joinToString(" ")
                    else -> ""
                }
            )
            else -> {
                println("Unknown command")
            }
        }
    }

    companion object {
        private const val COMMAND_EXIT = "exit"
    }
}