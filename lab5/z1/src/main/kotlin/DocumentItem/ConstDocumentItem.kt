package DocumentItem

import Image.IImage
import Paragraph.IParagraph

open class ConstDocumentItem: IConstDocumentItem {
    private val mImage: IImage? = null
    private val mParagraph: IParagraph? = null
    override fun getImage() = mImage
    override fun getParagraph() = mParagraph
}