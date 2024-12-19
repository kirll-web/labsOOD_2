import Canvas.Canvas
import Canvas.RectD
import Slide.Slide
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import common.Point
import kotlinx.coroutines.*
import shape.Figures.Ellipse
import shape.Figures.Rectangle
import shape.Figures.Triangle
import shape.Figures.TrianglePoints
import shape.Styles.FillStyle
import shape.ShapeGroup
import shape.Styles.StrokeStyle

typealias RGBAColor = Long


fun createSlideWithHome(): Slide {
    val slide = Slide()
    slide.setBackgroundColor(0xFF15c3ee)
    slide.insertShape(
        Rectangle(
            rect = RectD(
                0.0, 400.0, 800.0, 200.0
            ),
            fillStyle = FillStyle(0xFF19a141),
            strokeStyle = StrokeStyle(0u, null)
        ), 0u
    )

    //home
    slide.insertShape(
        ShapeGroup().apply {
            insertShape(
                Rectangle(
                    rect = RectD(
                        250.0, 300.0, 200.0, 200.0
                    ),
                    fillStyle = FillStyle(0xFFdf7a0f),
                    strokeStyle = StrokeStyle(0u, null)
                ), 0u
            )
            insertShape(
                Triangle(
                    TrianglePoints(
                        Point(200.0, 300.0),
                        Point(350.0, 200.0),
                        Point(500.0, 300.0)
                    ),
                    fillStyle = FillStyle(0xFFe92626),
                    strokeStyle = StrokeStyle(0u, null)
                ), 1u
            )
            insertShape(
                Ellipse(
                    RectD(
                        300.0, 350.0, 100.0, 100.0
                    ),
                    fillStyle = FillStyle(0x4c1DAEDE),
                    strokeStyle = StrokeStyle(0u, null)
                ), 2u
            )
        }, 1u
    )

    return slide
}

@Composable
@Preview

fun App() {
    val canvas = Canvas()
    var drawerState by remember {
        mutableStateOf(
            canvas.shapes
        )
    }


    val scope = CoroutineScope(Job() + Dispatchers.IO)

    scope.launch {
        delay(20L)
        val slide = createSlideWithHome()
        slide.drawSelf(canvas)
        val frame = slide.getShapeAtIndex(1u).getFrame()
        slide.getShapeAtIndex(1u).setFrame(
            RectD(
                150.0,
                150.0,
                frame?.let { it.width  * 1.5} ?: 0.0,
                frame?.let { it.height * 1.5 } ?: 0.0
            )
        )

        slide.draw(canvas)

        drawerState = canvas.shapes
    }

    MaterialTheme {
        drawerState

        Canvas(Modifier.fillMaxSize()) {
            canvas.draw(this)
        }
    }
}


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(width = 800.dp, height = 600.dp)
    ) {
        App()
    }
}
