package Designer

import Picture.PictureDraft

interface IDesigner {
    suspend fun createDraft(): PictureDraft
}