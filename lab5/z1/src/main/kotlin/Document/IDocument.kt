package Document

import DocumentItem.IConstDocumentItem
import DocumentItem.IDocumentItem
import Image.IImage
import Paragraph.IParagraph

interface IDocument
{

    // Вставляет параграф текста в указанную позицию (сдвигая последующие элементы)
    // Если параметр position не указан, вставка происходит в конец документа
    fun insertParagraph(text: String, position: Int): IParagraph

    // Вставляет изображение в указанную позицию (сдвигая последующие элементы)
    // Параметр path задает путь к вставляемому изображению
    // При вставке изображение должно копироваться в подкаталог images
    // под автоматически сгенерированным именем
    fun insertImage(path: String, width: Int, height: Int, position: Int): IImage

    // Возвращает количество элементов в документе
    fun getItemsCount(): Int

    // Доступ к элементам изображения
    fun getConstItem(index: Int): IConstDocumentItem
    fun getItem(index: Int): IDocumentItem

    // Удаляет элемент из документа
    fun deleteItem(index: IDocumentItem)

    // Возвращает заголовок документа
    fun getTitle(): String
    // Изменяет заголовок документа
    fun setTitle(title: String)
    fun canUndo(): Boolean
    // Сообщает о доступности операции Undo
    // Отменяет команду редактирования
    fun undo()

    // Сообщает о доступности операции Redo
    fun canRedo(): Boolean
    // Выполняет отмененную команду редактирования
    fun redo(): Boolean

    // Сохраняет документ в формате html. Изображения сохраняются в подкаталог images
    // пути к изображениям указываются относительно пути к сохраняемому HTML файлу
    fun save(path: String)
}