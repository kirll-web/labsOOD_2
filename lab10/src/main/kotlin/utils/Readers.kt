package utils

import Models.IConverter
import Models.IReader
import Models.ModelShape
import Models.ModelShapeJson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


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