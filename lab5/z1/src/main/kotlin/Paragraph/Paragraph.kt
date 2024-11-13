package Paragraph

import Command.FunctionalCommand
import History.History

class Paragraph(
    text: String,
    history: History
) : IParagraph {
    private var mText: String
    private val mHistoryController: History

    init {
        mText = text
        mHistoryController = history
    }

    override fun getText(): String = mText

    override fun setText(text: String) {
        val oldText = mText
        mHistoryController.addAndExecuteCommand(FunctionalCommand(
            doExecute = { this.mText = text },
            doUnexecute = { this.mText = oldText }
        ))
    }
}