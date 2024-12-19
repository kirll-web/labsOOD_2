import Models.Models
import View.ComposeCanvas
import View.ToolBar
import ViewModel.ComposeCanvasViewModel
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

const val WINDOW_WIDTH = 800
const val WINDOW_HEIGHT = 600
const val DEFAULT_WIDTH = 200
const val DEFAULT_HEIGHT = 200
const val TOOLBAR_HEIGHT = 50

typealias RGBAColor = ULong

@Composable
@Preview
fun App() {
    val dataModel = Models()
    val composeCanvasViewModel = ComposeCanvasViewModel(
        dataModel,
        WINDOW_WIDTH,
        WINDOW_HEIGHT,
        DEFAULT_WIDTH,
        DEFAULT_HEIGHT
    )
    val canvas = ComposeCanvas(composeCanvasViewModel)

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            ToolBar(composeCanvasViewModel, TOOLBAR_HEIGHT)
            canvas.draw()
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(width = WINDOW_WIDTH.dp, height = WINDOW_HEIGHT.dp),
    ) {
        App()
    }
}
