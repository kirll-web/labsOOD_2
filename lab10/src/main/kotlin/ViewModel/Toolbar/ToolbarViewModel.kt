package ViewModel.Toolbar

import Models.IModels
import Models.ModelShape
import Models.ModelsEvent
import ViewModel.ShapeFactory.IModelShapeFactory
import ViewModel.ShapeFactory.ShapeType
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

interface IToolbarViewModel {
    val events: SharedFlow<ToolbarEvent?>
    val viewModelScope: CoroutineScope
    fun addRectangle()
    fun addEllipse()
    fun addTriangle()
    suspend fun addImage()
    fun tryDeleteSelectedShape(): Boolean
    fun undo()
    fun redo()
    suspend fun save()
    suspend fun saveAs()
    suspend fun open()
}

sealed interface ToolbarEvent {
    data class OpenFileManagerForOpen(
        val description: String,
        val fmExtenstion: List<String>,
        val cb: (fileUrl: String) -> Unit
    ) : ToolbarEvent

    data class OpenFileManagerForSave(
        val description: String,
        val fmExtenstion: List<String>,
        val cb: (fileUrl: String) -> Unit
    ) : ToolbarEvent
}

data class ToolbarState(
    val openedFromFile: Boolean,
    val fileUrl: String? = null
)

class ToolbarViewModel(
    private val dataModel: IModels,
    private val mSelectedShapeId: MutableState<String?>,
    private val shapeFactory: IModelShapeFactory
) : IToolbarViewModel, ViewModel() {
    override val viewModelScope = CoroutineScope(Job() + Dispatchers.IO)
    private val mState = mutableStateOf(ToolbarState(false))
    private val mEvents = MutableSharedFlow<ToolbarEvent?>(0)
    override val events: SharedFlow<ToolbarEvent?> = mEvents

    init {
        viewModelScope.launch {
            delay(500)
            dataModel.events.onEach {
                when (it) {
                    ModelsEvent.OpenFileError -> mState.value = mState.value.copy(
                        openedFromFile = false,
                        fileUrl = null
                    )
                    is ModelsEvent.OpenFileSuccess -> {
                        mState.value = mState.value.copy(
                            openedFromFile = true,
                            fileUrl = it.fileUrl
                        )
                        mSelectedShapeId.value = null
                    }
                    else -> Unit
                }
            }.stateIn(viewModelScope)
        }
    }

    override fun tryDeleteSelectedShape(): Boolean {
        val id = mSelectedShapeId.value
        return when {
            id != null -> {
                mSelectedShapeId.value = null
                dataModel.removeShapeById(id)
                true
            }

            else -> false
        }
    }

    override fun addRectangle() {
        addShape(shapeFactory.createShape(ShapeType.RECTANGLE))
    }

    override fun addEllipse() {
        addShape(shapeFactory.createShape(ShapeType.ELLIPSE))
    }

    override fun addTriangle() {
        addShape(shapeFactory.createShape(ShapeType.TRIANGLE))
    }

    override suspend fun addImage() {
        /*imageDlgViewModel.show()*/
        mEvents.emit(
            ToolbarEvent.OpenFileManagerForOpen(
                "Text Files (*.png, *.jpeg, *.jpg)",
                fmExtenstion = listOf("png", "jpeg", "jpg"),
                cb = ::addImage
            )
        )
    }

    override fun undo() {
        dataModel.undo()
    }

    override fun redo() {
        dataModel.redo()
    }


    override suspend fun save() {
        if (mState.value.openedFromFile) saveInOpenedFile()
        else saveAs()
    }

    override suspend fun saveAs() {
        mEvents.emit(
            ToolbarEvent.OpenFileManagerForSave(
                "Text Files (*.json, *.xml)",
                fmExtenstion = listOf("json", "xml"),
                cb = ::saveAs
            )
        )
    }

    override suspend fun open() {
        mEvents.emit(
            ToolbarEvent.OpenFileManagerForOpen(
                "Text Files (*.json, *.xml)",
                fmExtenstion = listOf("json", "xml"),
                cb = ::open
            )
        )
    }

    private fun open(fileUrl: String) {
        dataModel.openFile(fileUrl)
    }

    private fun saveAs(fileUrl: String) {
        dataModel.saveFile(fileUrl)
    }

    private fun saveInOpenedFile() {
        mState.value.fileUrl?.let { dataModel.saveFile(it) }
    }

    private fun addImage(fileUrl: String) {
        addShape(shapeFactory.createShape(ShapeType.IMAGE, fileUrl))
    }

    private fun addShape(modelShape: ModelShape) {
        mSelectedShapeId.value = modelShape.id
        dataModel.tryAddShape(modelShape)
    }
}