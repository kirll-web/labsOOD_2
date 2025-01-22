package ViewModel

import Models.IModelShapes
import Models.ModelShapesEvent
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class ErrorState(
    val isShowError: Boolean,
    val message: String?
)

class ErrorDlgViewModel(
    private val dataModel: IModelShapes
): ViewModel() {
    private val mState = MutableSharedFlow<ErrorState>()
    val viewModelScope = CoroutineScope(Job() + Dispatchers.IO)

    val state: SharedFlow<ErrorState> = mState

    init {
        viewModelScope.launch {
            delay(500)
            dataModel.events.onEach {
                when (it) {
                    ModelShapesEvent.OpenFileError -> {
                        mState.emit(ErrorState(
                            true, "Ошибка при открытии файла"
                        ))
                        delay(3000)
                        mState.emit(ErrorState(
                            false, null
                        ))
                    }
                    ModelShapesEvent.ParsingFileError -> {
                        mState.emit(ErrorState(
                            true, "Ошибка при парсинге файла"
                        ))
                        delay(3000)
                        mState.emit(ErrorState(
                            false, null
                        ))
                    }
                    ModelShapesEvent.UnknowedFileExtension -> {
                        mState.emit(ErrorState(
                            true, "Выберите другой тип файла"
                        ))
                        delay(3000)
                        mState.emit(ErrorState(
                            false, null
                        ))
                    }
                    else -> Unit
                }
            }.launchIn(viewModelScope)
        }

    }

}