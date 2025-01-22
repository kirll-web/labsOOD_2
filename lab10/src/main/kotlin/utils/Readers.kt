package utils

import Models.ModelShape

interface IReader<T, K> {
    fun read(data: T, converter: IConverter<K>)
}

class ModelShapeReader: IReader<List<ModelShape>, List<ModelShape>> {
    override fun read(data: List<ModelShape>, converter: IConverter<List<ModelShape>>) {
        converter.convert(data)
    }
}


class TextReader: IReader<String, String> {
    override fun read(data: String, converter: IConverter<String>) {
        converter.convert(data)
    }
}