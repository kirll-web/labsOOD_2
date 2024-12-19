package DocumentItem

import Image.IImage
import Paragraph.IParagraph

class DocumentItem(
    paragraph: IParagraph? = null,
    image: IImage? = null
) : ConstDocumentItem() {
    private var mParagraph: IParagraph? = null
    private var mImage: IImage? = null

    init {
        mParagraph = paragraph
        mImage = image
    }

    override fun getImage(): IImage? {
        return mImage
    }

    override fun getParagraph(): IParagraph? {
        return mParagraph
    }

    override fun remove() {
        mImage?.remove()
    }
}
