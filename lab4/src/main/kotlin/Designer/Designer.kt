package Designer

import IShapeFactory.IShapeFactory
import Picture.IPictureDraft
import Picture.PictureDraft

class Designer(
    private val shapeFactory: IShapeFactory
): IDesigner {
    override suspend fun createDraft(): PictureDraft {
        val draft = PictureDraft()

        var exit = false
        while (!exit) {
            exit = execute(draft)
        }
        return draft
    }

    fun mockCreateDraft(list: List<String>): PictureDraft {
        val draft = PictureDraft()

        for (i in list) {
            try {
                val line = i
                if (line == EXIT) break
                shapeFactory.createShape(line)?.let { draft.addShape(it) }
            } catch(ex: Exception) {
                println(ex.message)
            }
        }

        return  draft
    }

    private fun execute(draft: PictureDraft): Boolean {
        try {
            val line = readln()
            if (line == EXIT) return true
            shapeFactory.createShape(line)?.let { draft.addShape(it) }
        } catch(ex: Exception) {
            println(ex.message)
        }
        return false
    }

    companion object {
        const val EXIT = "exit"
    }
}