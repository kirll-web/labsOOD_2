/*
package Legacy

import ViewModel.CanvasViewModel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.onClick
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CanvasArea(viewModel: CanvasViewModel) {
    val canvasState by viewModel.state
    Box(Modifier.fillMaxSize().background(Color.Red).onClick {
        viewModel.unslectPrimitive()
    }) {

        canvasState.shapes.forEach { shape ->
            shape.draw(
                {
                    viewModel.selectPrimitive(
                        shape.topLeft.x + shape.width / 2,
                        shape.topLeft.y + shape.height / 2
                    )
                },
                { x, y ->
                    viewModel.movePrimitive(x, y)
                }
            )
        }
        canvasState.selectedShape?.let { shape ->
            println("рисую ${canvasState}")
            shape.draw(
                {
                    viewModel.selectPrimitive(
                        shape.topLeft.x + shape.width / 2,
                        shape.topLeft.y + shape.height / 2
                    )
                },
                { x, y ->
                    viewModel.movePrimitive(x, y)
                }

            )
        }
    }
}*/
