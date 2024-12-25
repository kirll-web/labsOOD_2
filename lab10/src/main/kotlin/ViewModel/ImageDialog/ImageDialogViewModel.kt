package ViewModel.ImageDialog

import Models.IModels
import Models.ModelsEvent
import ViewModel.ShapeFactory.IModelShapeFactory
import ViewModel.ShapeFactory.ShapeType
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface IImageDialogViewModel {
    val isDialogVisible: Boolean
    val messageError: MutableState<String?>
    fun tryAddImage(url: String)
    fun setErrorMessage(message: String?)
    fun show()
    fun hide()
}

class ImageDialogViewModel(
    private val dataModel: IModels,
    private val shapeFactory: IModelShapeFactory
) : IImageDialogViewModel, ViewModel() {
    private var mIsDialogVisible = mutableStateOf(false)
    private val mErrorMessage = mutableStateOf<String?>(null)
    private val viewModelScope = CoroutineScope(Job() + Dispatchers.IO)
    override val isDialogVisible
        get() = mIsDialogVisible.value

    init {
        viewModelScope.launch {
            delay(500)
            dataModel.events.onEach { event ->
                when (event) {
                    ModelsEvent.AddShapeError -> setErrorMessage("Ошибка: Не удалось создать файл")
                    ModelsEvent.ShapeHasBeenAdded -> hide()
                    else -> Unit
                }
            }.launchIn(viewModelScope)
        }
    }

    override val messageError
        get() = mErrorMessage

    override fun tryAddImage(url: String) = dataModel.tryAddShape(
        shapeFactory.createShape(ShapeType.IMAGE, url)
    )

    override fun setErrorMessage(message: String?) {
        mErrorMessage.value = message
    }

    override fun show() {
        mIsDialogVisible.value =  true
    }

    override fun hide() {
        mIsDialogVisible.value =  false
    }
}
