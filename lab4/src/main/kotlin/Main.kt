import Canvas.Canvas
import Designer.Designer
import Designer.IDesigner
import IShapeFactory.ShapeFactory
import Painter.Painter
import Picture.PictureDraft
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
@Preview
fun App() {
    val canvas = Canvas()
    val pictureDraft by remember {
        mutableStateOf(
            PictureDraft()
        )
    }
    var drawerState by remember {
        mutableStateOf(
            canvas.shapes
        )
    }


    val rememberText = rememberTextMeasurer()

    val shapeFactory = ShapeFactory()
    val designer: IDesigner = Designer(shapeFactory)

    val scope = CoroutineScope(Job() + Dispatchers.IO)
    val painter = Painter()

    scope.launch {
        val draft = designer.createDraft()
        painter.drawPicture(draft, canvas)
        drawerState = canvas.shapes
    }

    MaterialTheme {
        pictureDraft
        drawerState
        Canvas(Modifier.fillMaxSize()) {
            canvas.draw(this, rememberText)
        }
    }
}


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
