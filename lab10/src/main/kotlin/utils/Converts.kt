package utils

import Models.ModelShape
import Models.ModelShapeJson
import ViewModel.IJsonMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken


interface IConverter<T> {
    fun convert(data: T)
}

class JsonConverter(
    private val mapperToJson: IJsonMapper<ModelShape, ModelShapeJson>
) : IConverter<ModelShape> {
    private val mGson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val mJsonObject = JsonObject()

    override fun convert(data: ModelShape) {
        val shape = mapperToJson.mapToJson(data)
        mJsonObject.add(data.id,  mGson.toJsonTree(shape))
    }

    fun getModelShapeJson(): String? {
        return mGson.toJson(mJsonObject)
    }
}

class ModelShapeJsonConverter: IConverter<String> {
    private val mGson: Gson = GsonBuilder().setPrettyPrinting().create()
    private var mShapes = emptyMap<String, ModelShapeJson>()

    fun getListModelShape(): Map<String, ModelShapeJson> = mShapes

    override fun convert(data: String) {
        val type = object : TypeToken<Map<String, ModelShapeJson>>() {}.type
        mShapes = mGson.fromJson(data, type)
    }
}