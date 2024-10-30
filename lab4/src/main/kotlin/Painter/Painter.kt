package Painter

import Canvas.ICanvas
import Picture.IPictureDraft

class Painter: IPainter {
    override fun drawPicture(draft: IPictureDraft, canvas: ICanvas) {
        for (i in 0 until draft.getShapeCount()) {
            draft.getShape(i).draw(canvas)
        }
    }
}