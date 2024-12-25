package View

import ViewModel.Toolbar.IToolbarViewModel
import ViewModel.Toolbar.ToolbarEvent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter


class Toolbar(
    private val viewModel: IToolbarViewModel,
    private val toolbarHeight: Int
) {
    init {
        viewModel.viewModelScope.launch {
            viewModel.events.onEach {
                when (it) {
                    is ToolbarEvent.OpenFileManagerForOpen -> {
                        val fileChooser = JFileChooser()
                        fileChooser.isAcceptAllFileFilterUsed = false
                        fileChooser.fileFilter = FileNameExtensionFilter(
                            it.description, *it.fmExtenstion.toTypedArray()
                        )
                        val result = fileChooser.showOpenDialog(null)

                        if (result == JFileChooser.APPROVE_OPTION) {
                            it.cb(fileChooser.selectedFile.absolutePath)
                        }
                    }

                    is ToolbarEvent.OpenFileManagerForSave -> {
                        val fileChooser = JFileChooser()
                        fileChooser.isAcceptAllFileFilterUsed = false
                        // Устанавливаем фильтр для типов файлов
                        fileChooser.isAcceptAllFileFilterUsed = false
                        fileChooser.fileFilter = FileNameExtensionFilter(
                            it.description, *it.fmExtenstion.toTypedArray()
                        )

                        // Открываем проводник в режиме сохранения файла
                        val result = fileChooser.showSaveDialog(null)

                        if (result == JFileChooser.APPROVE_OPTION) {
                           it.cb(fileChooser.selectedFile.absolutePath)
                        }
                    }

                    else -> Unit
                }

            }.stateIn(viewModel.viewModelScope)
        }
    }

    @Composable
    fun draw() {
        Row(Modifier.fillMaxWidth().padding(8.dp).height(toolbarHeight.dp)) {
            Button(onClick = {
                viewModel.viewModelScope.launch { viewModel.open() }
            }) {
                BasicText("open", style = TextStyle(color = Color.White))
            }

            Button(onClick = {
                viewModel.viewModelScope.launch { viewModel.save() }
            }) {
                BasicText("save", style = TextStyle(color = Color.White))
            }

            Button(onClick = {
                viewModel.viewModelScope.launch { viewModel.saveAs() }
            }) {
                BasicText("save as", style = TextStyle(color = Color.White))
            }

            Button(onClick = {
                viewModel.addRectangle()
            }) {
                BasicText("Добавить Прямоугольник", style = TextStyle(color = Color.White))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                viewModel.addEllipse()
            }) {
                BasicText("Добавить Эллипс", style = TextStyle(color = Color.White))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                viewModel.addTriangle()
            }) {
                BasicText("Добавить Треугольник", style = TextStyle(color = Color.White))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                viewModel.viewModelScope.launch { viewModel.addImage() }
            }) {
                BasicText("Добавить картинку", style = TextStyle(color = Color.White))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                if (!viewModel.tryDeleteSelectedShape())
                    println("Чтобы удалить фигуру, предварительно кликните по ней")
            }) {
                BasicText("Удалить Фигуру", style = TextStyle(color = Color.White))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                viewModel.undo()
            }) {
                BasicText("Undo", style = TextStyle(color = Color.White))
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                viewModel.redo()
            }) {
                BasicText("Redo", style = TextStyle(color = Color.White))
            }
        }
    }
}
