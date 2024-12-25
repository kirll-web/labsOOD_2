package utils

import Models.IReader
import Models.ModelShape


class ModelShapeReader: IReader<List<ModelShape>, ModelShape> {
    override fun read(data: List<ModelShape>, converter: IConverter<ModelShape>) {
        data.forEach {
            converter.convert(it)
        }
    }
}

class TextReader: IReader<String, String> {
    override fun read(data: String, converter: IConverter<String>) {
        converter.convert(data)
    }
}