package utils

import Models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

import org.simpleframework.xml.core.Persister


interface IConverter<T> {
    fun convert(data: T)
}

@Root(name = "ModelShapeXML")
data class ModelShapeXML(
    @field:Element(name = "id")
    var id: String = "",
    @field:Element(name = "type")
    var type: String = "",
    @field:Element(name = "x")
    var x: Float = 0f,
    @field:Element(name = "y")
    var y: Float = 0f,
    @field:Element(name = "width")
    var width: Float = 0f,
    @field:Element(name = "height")
    var height: Float = 0f,
    @field:Element(name = "color")
    var color: ULong = 0u,
    @field:Element(name = "url", required = false)
    var url: String? = ""
) {
    constructor() : this("", "", 0f, 0f, 0f, 0f, 0u, "")
}

@Root(name = "ModelShapesXML")
data class ModelShapesXML(
    @field:ElementList(inline = true) var shapes: MutableList<ModelShapeXML> =  mutableListOf()
) {
    constructor() : this(mutableListOf())
}

data class ModelShapeJson(
    val id: String,
    val type: String,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val color: ULong,
    val url: String?
)

class ModelShapeToJsonConverter(
    private val mapperToJson: IJsonMapper<ModelShape, ModelShapeJson>
) : IConverter<List<ModelShape>> {
    private val mGson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val mJsonObject = JsonObject()

    override fun convert(data: List<ModelShape>) {
        data.forEach {
            val shape = mapperToJson.mapToJson(it)
            mJsonObject.add(shape.id, mGson.toJsonTree(shape))
        }
    }

    fun getModelShapeJson(): String? {
        return mGson.toJson(mJsonObject)
    }
}

class ModelShapeFromJsonConverter(
    private val mapperJson: IJsonMapper<ModelShape, ModelShapeJson>
) : IConverter<String> {
    private val mGson: Gson = GsonBuilder().setPrettyPrinting().create()
    private var mShapes = emptyMap<String, ModelShape>()

    fun getModelShapes(): Map<String, ModelShape> = mShapes

    override fun convert(data: String) {
        val type = object : TypeToken<Map<String, ModelShapeJson>>() {}.type
        mShapes = mGson.fromJson<Map<String, ModelShapeJson>?>(data, type).map {
            it.key to mapperJson.mapFromJson(it.value)
        }.toMap()
    }
}


class ModelShapeToXMLConverter(
    private val mapperXML: IXMLMapper<ModelShape, ModelShapeXML>
) : IConverter<List<ModelShape>> {

    private var mShapes = ""

    override fun convert(data: List<ModelShape>) {
        val modelShapes = ModelShapesXML(
            shapes = data.map { mapperXML.mapToXML(it) }.toMutableList()
        )
        val serializer = Persister()
        val file = kotlin.io.path.createTempFile().toFile()
        serializer.write(modelShapes,file)

        mShapes = file.readText()
        file.delete()
    }

    fun getModelShapesXML() = mShapes
}


class ModelShapeFromXMLConverter(
    private val mapperXML: IXMLMapper<ModelShape, ModelShapeXML>
) : IConverter<String> {
    private var mShapes = emptyMap<String, ModelShape>()

    fun getModelShapes(): Map<String, ModelShape> = mShapes

    override fun convert(data: String) {
        val serializer = Persister()
        mShapes = serializer.read(ModelShapesXML::class.java, data).shapes.map {
            it.id to mapperXML.mapFromXML(it)
        }.toMap()
    }
}
