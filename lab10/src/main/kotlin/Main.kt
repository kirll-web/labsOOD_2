import utils.JsonMapper
import Models.ModelShapes
import utils.XMLMapper
import View.ComposeCanvas
import View.ErrorDlg
import View.Toolbar
import ViewModel.Canvas.ComposeCanvasViewModel
import ViewModel.ErrorDlgViewModel
import ViewModel.Mapper
import ViewModel.ShapeFactory.ShapeFactory
import ViewModel.Toolbar.ToolbarViewModel
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import utils.ModelShapeReader
import utils.TextReader

const val WINDOW_WIDTH = 1500f
const val WINDOW_HEIGHT = 900f
const val DEFAULT_WIDTH = 200f
const val DEFAULT_HEIGHT = 200f
const val TOOLBAR_HEIGHT = 50

typealias RGBAColor = ULong

class App {
    private val dataModel = ModelShapes(
        ModelShapeReader(),
        TextReader(),
        JsonMapper(),
        XMLMapper()
    )
    private val errorDlgViewModel = ErrorDlgViewModel(dataModel)
    private val errorDlg = ErrorDlg(errorDlgViewModel)

    private val mSelectedShapeId = mutableStateOf<String?>("")
    private val composeCanvasViewModel = ComposeCanvasViewModel(
        dataModel,
        WINDOW_WIDTH,
        WINDOW_HEIGHT,
        Mapper(),
        mSelectedShapeId
    )
    private val shapeFactory = ShapeFactory(
        WINDOW_WIDTH,
        WINDOW_HEIGHT,
        DEFAULT_WIDTH,
        DEFAULT_HEIGHT,
    )

    private val toolbarViewModel = ToolbarViewModel(
        dataModel,
        mSelectedShapeId,
        shapeFactory
    )
    private val canvas = ComposeCanvas(composeCanvasViewModel)
    private val toolbar = Toolbar(toolbarViewModel, TOOLBAR_HEIGHT)


    @Composable
    @Preview
    fun create() {
        MaterialTheme {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    toolbar.draw()
                    canvas.draw()
                }
                Box(
                    modifier = Modifier.align(Alignment.Center),
                    content = { errorDlg.draw() }
                )
            }
        }
    }
}


fun main() = application {
    val app = App()
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(width = WINDOW_WIDTH.dp, height = WINDOW_HEIGHT.dp),
    ) {
        app.create()
    }
}
