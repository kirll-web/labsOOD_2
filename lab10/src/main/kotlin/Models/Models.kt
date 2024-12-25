package Models

import Command.AddShapeCommand
import Command.DeleteItemCommand
import Command.MoveShapeCommand
import Command.ResizeShapeCommand
import History.History
import ViewModel.IJsonMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import utils.IConverter
import utils.JsonConverter
import utils.ModelShapeJsonConverter
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.createTempDirectory

const val MAX_LENGTH_TEMP_NAME = 12

//переименовать
interface IModels {
    val shapes: StateFlow<Map<String, ModelShape>>
    val events: SharedFlow<ModelsEvent?>
    fun tryAddShape(shape: ModelShape, position: Int? = null)
    fun moveShape(shape: ModelShape)
    fun resizeShape(shape: ModelShape)
    fun removeShapeById(id: String)
    fun undo()
    fun redo()
    fun openFile(filePath: String)
    fun saveFile(filePath: String)
}

sealed interface ModelsEvent {
    object AddShapeError: ModelsEvent
    object ShapeHasBeenAdded: ModelsEvent
    data class OpenFileSuccess(val fileUrl: String): ModelsEvent
    object OpenFileError: ModelsEvent
    object UnknowedFileExtension: ModelsEvent
    data class SelectShape(val id: String): ModelsEvent
}

interface IReader<T, K> {
    fun read(data: T, converter: IConverter<K>)
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


class Models(
    private val modelShapeReader: IReader<List<ModelShape>, ModelShape>,
    private val textReader: IReader<String, String>,
    private val mapper: IJsonMapper<ModelShape, ModelShapeJson>
) : IModels {
    private val mTempFolder = createTempDirectory()
    private val mShapes = MutableStateFlow(mapOf<String, ModelShape>())
    private val mHistory = History()
    private val mEvents = MutableSharedFlow<ModelsEvent?>(0)


    enum class SupportedFileTypes(val ext: String) {
        XML("xml"),
        JSON("json")
    }

    enum class SupportedImageTypes(val ext: String) {
        PNG("png"),
        JPG("jpg"),
        JPEG("jpeg")
    }

    override val shapes: StateFlow<Map<String, ModelShape>> = mShapes
    override val events: SharedFlow<ModelsEvent?> = mEvents

    override fun tryAddShape(shape: ModelShape, position: Int?) {
        mEvents.tryEmit(ModelsEvent.AddShapeError)
        if (shape is ModelShape.Image) {
            val file = File(shape.url)
            if (!file.exists()) {
                mEvents.tryEmit(ModelsEvent.AddShapeError)
                return
            }

            val extension = file.name
            val to = "${mTempFolder}/${generateRandomFileName(MAX_LENGTH_TEMP_NAME)}$extension"
            shape.setUrl(to)
            Files.copy(Path(file.path), Path(to), StandardCopyOption.REPLACE_EXISTING)
            mEvents.tryEmit(ModelsEvent.ShapeHasBeenAdded)
        }

        mHistory.addAndExecuteCommand(
            AddShapeCommand(mShapes, shape, position)
        )
    }

    override fun resizeShape(shape: ModelShape) {
        val oldShape = mShapes.value[shape.id]

        oldShape?.let {
            mHistory.addAndExecuteCommand(
                ResizeShapeCommand(
                    {
                        mShapes.value = mShapes.value.toMutableMap().apply {
                            this[shape.id] = shape
                        }
                    },
                    {
                        mShapes.value = mShapes.value.toMutableMap().apply {
                            this[shape.id] = oldShape
                            tryEventEmit(ModelsEvent.SelectShape(shape.id))
                        }
                    }
                )
            )
        }
    }

    override fun moveShape(shape: ModelShape) {
        val oldShape = mShapes.value[shape.id]

        oldShape?.let {
            mHistory.addAndExecuteCommand(
                MoveShapeCommand(
                    {
                        mShapes.value = mShapes.value.toMutableMap().apply {
                            this[shape.id] = shape
                        }
                    },
                    {
                        mShapes.value = mShapes.value.toMutableMap().apply {
                            this[shape.id] = oldShape
                            tryEventEmit(ModelsEvent.SelectShape(shape.id))
                        }
                    }
                )
            )
        }
    }


    override fun removeShapeById(id: String) {

        mShapes.value[id]?.let { shape ->
            mHistory.addAndExecuteCommand(
                DeleteItemCommand(mShapes, shape, mShapes.value.keys.indexOf(shape.id)) {
                    tryEventEmit(ModelsEvent.SelectShape(shape.id))
                }
            )
        }
    }

    override fun undo() {
        if (mHistory.canUndo()) {
            mHistory.undo()
        }
    }

    override fun redo() {
        if (mHistory.canRedo()) {
            mHistory.redo()
        }
    }

    override fun openFile(filePath: String) {
        val file = File(filePath)
        if(!file.exists()) {
            tryEventEmit(ModelsEvent.OpenFileError)
            return
        }
        val fileExt = SupportedFileTypes.entries.find { it.ext == file.extension }

        if(fileExt == null) {
            tryEventEmit(ModelsEvent.UnknowedFileExtension)
            return
        }

        when(fileExt) {
            SupportedFileTypes.XML -> {
                val jsonReader = ModelShapeJsonConverter()
                textReader.read(file.readText(), jsonReader)
                val s =  jsonReader.getListModelShape()
                mShapes.value = jsonReader.getListModelShape().map {
                    it.key to mapper.mapFromJson(it.value)
                }.toMap()
                tryEventEmit(ModelsEvent.OpenFileSuccess(filePath))
            }
            SupportedFileTypes.JSON -> {
                val jsonReader = ModelShapeJsonConverter()
                textReader.read(file.readText(), jsonReader)
                mShapes.value = jsonReader.getListModelShape().map {
                    it.key to mapper.mapFromJson(it.value)
                }.toMap()
                tryEventEmit(ModelsEvent.OpenFileSuccess(filePath))

            }
        }
    }

    override fun saveFile(filePath: String) {
        val jsonConverter = JsonConverter(mapper)
        modelShapeReader.read(mShapes.value.values.toList(), jsonConverter)
            val s = jsonConverter.getModelShapeJson()
        s?.let { File(filePath).writeText(it) }
    }

    private fun tryEventEmit(event: ModelsEvent) {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            mEvents.emit(event)
        }
    }
}


sealed class ModelShape(
    open val id: String,
    open val x: Float = 0f,
    open val y: Float = 0f,
    open val width: Float = 0f,
    open val height: Float = 0f,
    open val color: ULong = 0x000000uL
) {
    open fun remove() {}

    class Rectangle(
        id: String,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: ULong
    ) : ModelShape(
        id,
        x, y, width, height, color
    )

    class Ellipse(
        id: String,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: ULong
    ) : ModelShape(
        id,
        x, y, width, height, color
    )

    class Triangle(
        id: String,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: ULong
    ) : ModelShape(
        id,
        x, y, width, height, color
    )


    class Image(
        id: String, x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: ULong,
        imgURL: String
    ) : ModelShape(
        id,
        x, y, width, height, color
    ) {
        private var mUrl = imgURL
        val url
            get() = mUrl

        override fun remove() {
            val file = when {
                File(mUrl).exists() -> File(mUrl)
                else -> return
            }

            file.delete()
        }

        fun setUrl(url: String) { mUrl = url }
    }
}

fun generateRandomFileName(fileNameLength: Int): String {
    val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    return (1..fileNameLength).map { allowedChars.random() }.joinToString("")
}


