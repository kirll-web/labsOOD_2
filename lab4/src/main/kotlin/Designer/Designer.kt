package Designer

import IShapeFactory.IShapeFactory
import Picture.PictureDraft

class Designer(
    private val shapeFactory: IShapeFactory
): IDesigner {
    override suspend fun createDraft(): PictureDraft {
        val draft = PictureDraft()

        while (true) {
            try {
                val line = readln()
                if (line == EXIT) break
                shapeFactory.createShape(line)?.let { draft.addShape(it) }
            } catch(ex: Exception) {
                println(ex.message)
            }
        }
        return  draft
    }

    companion object {
        const val EXIT = "exit"
    }
}