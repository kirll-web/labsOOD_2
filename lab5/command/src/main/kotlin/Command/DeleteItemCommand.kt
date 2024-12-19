package org.command.Command

import DocumentItem.DocumentItem

class DeleteItemCommand(
    private var items: List<DocumentItem>,
    private val value: DocumentItem,
    private var position: Int? = null
) : AbstractCommand() {
    override fun doExecute() {
        val it = items.find {
            it == value
        }
        it?.let {
            items = items.minus(it)
        }
    }
    override fun doUnexecute() {
        position?.let {
            val newList = items.toMutableList()
            newList[it] = value
            items = newList.toList()
        }
    }
}

