/*
package Legacy

import Model.Shape
import ViewModel.CanvasViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.*

package ui

import ViewModel.CanvasViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import Model.Shape
import ViewModel.copy
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.*

val scope = CoroutineScope(Job() + Dispatchers.IO)
@Composable
fun CanvasAreaOld(viewModel: CanvasViewModel) {
    Box(Modifier.fillMaxSize().background(Color.LightGray)) {
        var canvasState by remember { mutableStateOf(viewModel.canvasState.shapes) }
        var selectedShape by remember { mutableStateOf<Shape?>(null)}
        val scope = CoroutineScope(Job() + Dispatchers.IO)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            // Найти фигуру, по которой кликнули
                            selectedShape = viewModel.canvasState.shapes.values.find { it.contains(offset.x, offset.y) }
                        },
                        onDragCancel = {},
                        onDragEnd = {
                            selectedShape = null
                        }
                    ) { c, dragAmount ->
                        selectedShape?.let { it ->
                            selectedShape = viewModel.moveShape(it, dragAmount.x, dragAmount.y)
                            selectedShape?.let { canvasState.put(it.id, it) }
                        }
                        c.consume()
                    }
                }
        ) {
            selectedShape
            canvasState.values.forEach { shape ->
                when (shape) {
                    is Shape.Rectangle -> drawRect(
                        color = if (shape == viewModel.canvasState.selectedShape) Color.Green else Color.Blue,
                        topLeft = Offset(shape.x, shape.y),
                        size = Size(shape.width, shape.height)
                    )
                    is Shape.Ellipse -> drawOval(
                        color = if (shape == viewModel.canvasState.selectedShape) Color.Green else Color.Red,
                        topLeft = Offset(shape.x, shape.y),
                        size = Size(shape.width, shape.height)
                    )
                    is Shape.Triangle -> {
                        // Нарисуй треугольник
                    }
                }
            }
        }
    }


}


*/
/*
*
*  onDragStart = { offset ->
                            println("Drag started at $offset")
                            scope.launch {
                                val shape = canvasState.shapes.find { it.contains(offset.x, offset.y) }
                                viewModel.canvasState.value.selectedShape = shape
                                draggingShape = shape
                            }

                        },
                        onDrag = { change, dragAmount ->
                            draggingShape?.let { shape ->
                                println("Dragging shape: $shape by $dragAmount")
                                scope.launch {
                                    viewModel.moveShape(dragAmount.x, dragAmount.y)
                                    change.consume()
                                }
                            }
                        },
                        onDragEnd = {
                            println("Drag ended")
                            scope.launch {
                                draggingShape = null
                            }
                        }
* */

