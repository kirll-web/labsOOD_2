import java.util.*

data class MenuItem(
    val name: String,
    val description: String,
    val command: () -> Unit
)

class Menu {
    private var mMenuItems = listOf<MenuItem>()

    fun addItem(name: String, description: String, command: () -> Unit) {
        mMenuItems = mMenuItems.plus(MenuItem(
            name = name,
            description = description,
            command = command
        ))
    }

    fun run() {
        showMenu()

        var command: String
        var isCanExit = false
        while (!isCanExit) {
            command = readln()

            when {
                //TODO НАДО СДЕЛАТЬ ПО ДРУГОМУ. ТУТ НАДО ПРОЧИТАТЬ ВСЕ ЗНАЧЕНИЯ MENU_ITEM
                command.lowercase(Locale.getDefault()) == COMMAND_EXIT -> {
                    isCanExit = true
                }
            }
        }
    }

    fun showMenu() {
        mMenuItems.forEach {
            println("${it.name}: ${it.description}")
        }
    }

    fun exit() {}


    companion object {
        private const val COMMAND_EXIT = "exit"
    }
}