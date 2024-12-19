package View

import ViewModel.IMenuViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ToolBar(
    canvasViewModel: IMenuViewModel,
    toolbarHeight: Int,
) {
    Row(Modifier.fillMaxWidth().padding(8.dp).height(toolbarHeight.dp)) {
        Button(onClick = {
            canvasViewModel.addRectangle()
        }) {
            BasicText("Добавить Прямоугольник")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            canvasViewModel.addEllipse()
        }) {
            BasicText("Добавить Эллипс")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            canvasViewModel.addTriangle()
        }) {
            BasicText("Добавить Треугольник")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            if (!canvasViewModel.tryDeleteSelectedShape()) println("Чтобы удалить фигуру, предварительно кликните по ней")
            // todo переделать на нормальный тост
        }) {
            BasicText("Удалить Фигуру")
        }
        Button(onClick = {
            canvasViewModel.undo()
        }) {
            BasicText("Undo")
        }
        Button(onClick = {
            canvasViewModel.redo()
        }) {
            BasicText("Red")
        }
    }
}