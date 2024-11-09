package org.command.Command

import DocumentItem.ConstDocumentItem
import DocumentItem.DocumentItem

class InsertCommand(
    private var items: MutableList<DocumentItem>,
    value: DocumentItem,
    private val position: Int
): AbstractCommand() {
    private val mValue = value
    val value: ConstDocumentItem
        get() = mValue

    private var mOldValue: DocumentItem? = null
    override fun doExecute() {
        when {
            position == items.size -> items.add(mValue)
            else -> {
                mOldValue = items[position]
                items[position] = mValue
            }
        }
    }

    override fun doUnexecute() {
        val it = items.find {
            it == mValue
        }
        it?.let {
            when {
                mOldValue == null -> items.remove(it)
                else  -> items[position] = mOldValue!!
            }
        }
    }
}