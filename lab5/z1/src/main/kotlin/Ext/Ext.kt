package Ext

fun String.isInt(): Boolean = try {
    val n = this.toInt()
    true
} catch (ex: Exception) {
    false
}

fun List<String>.isListInt(): Boolean {
    this.forEach {
        if (!it.isInt()) return false
    }
    return true
}