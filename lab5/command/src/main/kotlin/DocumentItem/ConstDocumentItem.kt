package DocumentItem

import Image.IImage
import Paragraph.IParagraph

open class ConstDocumentItem(
    paragraph: IParagraph? = null,
    image: IImage? = null
) {
    private val mImage: IImage? = image
    private val mParagraph: IParagraph? = paragraph
    open fun getImage() = mImage
    open fun getParagraph() = mParagraph
}