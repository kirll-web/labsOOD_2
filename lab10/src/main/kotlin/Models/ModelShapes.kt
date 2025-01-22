package Models

import Command.AddShapeCommand
import Command.DeleteItemCommand
import Command.MoveShapeCommand
import Command.ResizeShapeCommand
import History.History
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import utils.*
import java.io.Closeable
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.createTempDirectory
import kotlin.io.path.extension
import kotlin.io.path.pathString

const val MAX_LENGTH_TEMP_NAME = 12

//переименовать
interface IModelShapes {
    val shapes: StateFlow<Map<String, ModelShape>>
    val events: SharedFlow<ModelShapesEvent?>
    fun tryAddShape(shape: ModelShape, position: Int? = null)
    fun moveShape(shape: ModelShape)
    fun resizeShape(shape: ModelShape)
    fun removeShapeById(id: String)
    fun undo()
    fun redo()
    fun openFile(filePath: String)
    fun saveFile(filePath: String)
}

sealed interface ModelShapesEvent {
    object AddShapeError : ModelShapesEvent
    object ShapeHasBeenAdded : ModelShapesEvent
    data class OpenFileSuccess(val fileUrl: String) : ModelShapesEvent
    object OpenFileError : ModelShapesEvent
    object UnknowedFileExtension : ModelShapesEvent
    object ParsingFileError : ModelShapesEvent
    data class SelectShape(val id: String) : ModelShapesEvent
}



class ModelShapes(
    private val modelShapeReader: IReader<List<ModelShape>, List<ModelShape>>,
    private val textReader: IReader<String, String>,
    private val jsonMapper: IJsonMapper<ModelShape, ModelShapeJson>,
    private val xmlMapper: IXMLMapper<ModelShape, ModelShapeXML>
) : IModelShapes, Closeable {
    private val mTempFolder = createTempDirectory()
    private val mShapes = MutableStateFlow(mapOf<String, ModelShape>())
    private val mHistory = History()
    private val mEvents = MutableSharedFlow<ModelShapesEvent?>(0)


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
    override val events: SharedFlow<ModelShapesEvent?> = mEvents

    override fun tryAddShape(shape: ModelShape, position: Int?) {
        mEvents.tryEmit(ModelShapesEvent.AddShapeError)

        if (shape is ModelShape.Image) {
            val file = File(shape.url)
            if (!file.exists()) {
                mEvents.tryEmit(ModelShapesEvent.AddShapeError)
                return
            }

            val extension = file.name
            val to = "${mTempFolder}/${generateRandomFileName(MAX_LENGTH_TEMP_NAME)}$extension"
            shape.setUrl(to)
            Files.copy(Path(file.path), Path(to), StandardCopyOption.REPLACE_EXISTING)
            mEvents.tryEmit(ModelShapesEvent.ShapeHasBeenAdded)
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
                            tryEventEmit(ModelShapesEvent.SelectShape(shape.id))
                        }
                    },
                    shape
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
                            tryEventEmit(ModelShapesEvent.SelectShape(shape.id))
                        }
                    },
                    shape
                )
            )
        }
    }


    override fun removeShapeById(id: String) {
        mShapes.value[id]?.let { shape ->
            mHistory.addAndExecuteCommand(
                DeleteItemCommand(mShapes, shape, mShapes.value.keys.indexOf(shape.id)) {
                    tryEventEmit(ModelShapesEvent.SelectShape(shape.id))
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
        if (!file.exists()) {
            tryEventEmit(ModelShapesEvent.OpenFileError)
            return
        }
        val fileExt = SupportedFileTypes.entries.find { it.ext == file.extension }

        if (fileExt == null) {
            tryEventEmit(ModelShapesEvent.UnknowedFileExtension)
            return
        }

        when (fileExt) {
            SupportedFileTypes.XML -> {
                try {
                    val converter = ModelShapeFromXMLConverter(xmlMapper)
                    textReader.read(file.readText(), converter)
                    val shapes = converter.getModelShapes()
                    shapes.values.forEach {
                        if (it is ModelShape.Image) {

                            val img = File(file.parent + it.url)
                            if (!file.exists()) {
                                mEvents.tryEmit(ModelShapesEvent.AddShapeError)
                                return
                            }

                            val extension = img.name
                            val to = "${mTempFolder}/${generateRandomFileName(MAX_LENGTH_TEMP_NAME)}$extension"
                            it.setUrl(to)
                            Files.copy(Path(img.path), Path(to), StandardCopyOption.REPLACE_EXISTING)
                        }
                    }
                    mShapes.value = converter.getModelShapes()
                    tryEventEmit(ModelShapesEvent.OpenFileSuccess(filePath))
                } catch (ex: Exception) {
                    tryEventEmit(ModelShapesEvent.ParsingFileError)
                }
            }

            SupportedFileTypes.JSON -> {
                try {
                    val converter = ModelShapeFromJsonConverter(jsonMapper)
                    textReader.read(file.readText(), converter)
                    val shapes = converter.getModelShapes()
                    shapes.values.forEach {
                        if (it is ModelShape.Image) {
                            val img = File(file.parent + it.url)
                            println(file.parent + it.url)
                            if(img.exists()) println("тут падает")
                            val newImagePath = Path(
                                "${mTempFolder}\\${Path(img.path).fileName}"
                            )

                            Files.copy(
                                Path(img.path),
                                newImagePath,
                                StandardCopyOption.REPLACE_EXISTING
                            )

                            it.setUrl(newImagePath.pathString)
                        }

                    }
                    mShapes.value = shapes
                    tryEventEmit(ModelShapesEvent.OpenFileSuccess(filePath))
                } catch (ex: Exception) {
                    println(ex)
                    tryEventEmit(ModelShapesEvent.ParsingFileError)
                }
            }
        }
    }

    override fun saveFile(filePath: String) {
        val file = File(filePath)
        if (!file.exists()) {
            file.createNewFile()
        }
        val fileExt = SupportedFileTypes.entries.find { it.ext == file.extension }


        when (fileExt) {
            SupportedFileTypes.XML -> {
                val imgDir = File("${file.parent}/imagesForPainting")
                imgDir.mkdirs()
                val shapes = mShapes.value.values
                shapes.forEach {
                    if (it is ModelShape.Image) {
                        val img = File(it.url)
                        val newImagePath = Path(
                            "${imgDir}\\${Path(img.path).fileName}"
                        )

                        Files.copy(
                            Path(it.url),
                            newImagePath,
                            StandardCopyOption.REPLACE_EXISTING
                        )

                        it.setUrl("/imagesForPainting/${newImagePath.fileName}")
                    }
                }
                val converter = ModelShapeToXMLConverter(xmlMapper)
                modelShapeReader.read(shapes.toList(), converter)
                file.writeText(converter.getModelShapesXML())
            }

            SupportedFileTypes.JSON -> {
                val imgDir = File("${file.parent}/imagesForPainting")
                imgDir.mkdirs()
                val shapes = mShapes.value.values
                shapes.forEach {
                    if (it is ModelShape.Image) {
                        val img = File(it.url)
                        val newImagePath = Path(
                            "${imgDir}\\${Path(img.path).fileName}"
                        )

                        Files.copy(
                            Path(it.url),
                            newImagePath,
                            StandardCopyOption.REPLACE_EXISTING
                        )

                        it.setUrl("/imagesForPainting/${newImagePath.fileName}")
                    }
                }

                val converter = ModelShapeToJsonConverter(jsonMapper)
                modelShapeReader.read(shapes.toList(), converter)
                converter.getModelShapeJson()?.let { File(filePath).writeText(it) }
            }

            else -> tryEventEmit(ModelShapesEvent.UnknowedFileExtension)
        }
    }

    private fun tryEventEmit(event: ModelShapesEvent) {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            mEvents.emit(event)
        }
    }

    override fun close() {
        mShapes.value.values.forEach {
            it.remove()
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
            println("remove")
            file.delete()
        }

        fun setUrl(url: String) {
            mUrl = url
        }
    }
}

fun generateRandomFileName(fileNameLength: Int): String {
    val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    return (1..fileNameLength).map { allowedChars.random() }.joinToString("")
}


