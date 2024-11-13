package Command

import DocumentItem.DocumentItem

class InsertItemCommand(
    private var items: List<DocumentItem>,
    private val value: DocumentItem,
    private var position: Int? = null
) : AbstractCommand() {
    override fun doExecute() {
        position?.let {
            val newList = items.toMutableList()
            newList[it] = value
            items = newList.toList()
        }
    }
    override fun doUnexecute() {
        val it = items.find {
            it == value
        }
        it?.let {
            items = items.minus(it)
        }
    }

}

