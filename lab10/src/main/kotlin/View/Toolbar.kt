package View

import ViewModel.IMenuViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp


class Toolbar(
    private val viewModel: IMenuViewModel,
    private val toolbarHeight: Int
) {
    @Composable
    fun draw() {
        Row(Modifier.fillMaxWidth().padding(8.dp).height(toolbarHeight.dp)) {
            Button(onClick = {
                viewModel.addRectangle()
            }) {
                BasicText("Добавить Прямоугольник",  style = TextStyle(color = Color.White))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                viewModel.addEllipse()
            }) {
                BasicText("Добавить Эллипс",  style = TextStyle(color = Color.White))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                viewModel.addTriangle()
            }) {
                BasicText("Добавить Треугольник",  style = TextStyle(color = Color.White))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (!viewModel.tryDeleteSelectedShape()) println("Чтобы удалить фигуру, предварительно кликните по ней")
                // todo переделать на нормальный тост
            }) {
                BasicText("Удалить Фигуру",  style = TextStyle(color = Color.White))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                viewModel.undo()
            }) {
                BasicText("Undo",  style = TextStyle(color = Color.White))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                viewModel.redo()
            }) {
                BasicText("Redo",  style = TextStyle(color = Color.White))
            }
        }
    }
}
