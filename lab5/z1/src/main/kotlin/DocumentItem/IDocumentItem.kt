package DocumentItem

import Image.IImage
import Paragraph.IParagraph

interface IConstDocumentItem  {
    // Возвращает указатель на константное изображение, либо nullptr, если элемент не является изображением
    fun getImage(): IImage
    // Возвращает указатель на константный параграф, либо nullptr, если элемент не является параграфом
    fun getParagraph(): IParagraph
}

/*
Элемент документа. Позволяет получить доступ к изображению или параграфу
*/
interface IDocumentItem : IConstDocumentItem
{

    // Возвращает указатель на изображение, либо nullptr, если элемент не является изображением
    override fun getImage(): IImage
    // Возвращает указатель на параграф, либо nullptr, если элемент не является параграфом
    override fun getParagraph(): IParagraph
}