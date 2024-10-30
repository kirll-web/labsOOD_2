package Painter

import Canvas.ICanvas
import Picture.IPictureDraft
import Picture.PictureDraft

interface IPainter {
    fun drawPicture(draft: IPictureDraft, canvas: ICanvas)
}