package Legacy

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.onClick
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

sealed class Primitive(
    open val topLeft: IntOffset,
    open val width: Float,
    open val height: Float,
    open val color: Color
) {
    @Composable
    abstract fun draw(onClick: () -> Unit, onMove: (x: Float, y: Float) -> Unit,)


    data class Rectangle(
        override val topLeft: IntOffset,
        override val width: Float,
        override val height: Float,
        override val color: Color,
    ): Primitive(topLeft, width, height, color) {
        @Composable
        override fun draw(onClick: () -> Unit, onMove: (x: Float, y: Float) -> Unit,) {
            drawImpl(onClick, onMove)
        }
    }

    data class Ellipse(
        override val topLeft: IntOffset,
        override val width: Float,
        override val height: Float,
        override val color: Color,
    ): Primitive(topLeft, width, height, color) {
        @Composable
        override fun draw(onClick: () -> Unit, onMove: (x: Float, y: Float) -> Unit,) {
            drawImpl(onClick,onMove, EllipseShape)
        }
        private object EllipseShape : Shape {
            override fun createOutline(
                size: androidx.compose.ui.geometry.Size,
                layoutDirection: androidx.compose.ui.unit.LayoutDirection,
                density: androidx.compose.ui.unit.Density
            ): Outline {
                return Outline.Generic(
                    Path().apply {
                        addOval(Rect(0f, 0f, size.width, size.height))
                    }
                )
            }
        }
    }

    data class Triangle(
        override val topLeft: IntOffset,
        override val width: Float,
        override val height: Float,
        override val color: Color,
    ):  Primitive(topLeft, width, height, color) {
        @Composable
        override fun draw(onClick: () -> Unit, onMove: (x: Float, y: Float) -> Unit,) {
            drawImpl(onClick, onMove, TriangleShape)
        }

        private object TriangleShape : Shape {
            override fun createOutline(
                size: androidx.compose.ui.geometry.Size,
                layoutDirection: androidx.compose.ui.unit.LayoutDirection,
                density: androidx.compose.ui.unit.Density
            ) = Outline.Generic(
                Path().apply {
                    moveTo(size.width / 2, 0f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
            )
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    protected fun drawImpl(
        onClick: () -> Unit,
        onMove: (x: Float, y: Float) -> Unit,
        shape: Shape? = null
    ) {

        Box(
            Modifier
                .offset { topLeft }
                .size(DpSize(width.dp,height.dp))
                .onClick {
                    onClick()
                }.background(color, shape ?: RectangleShape).pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {},
                    onDragCancel = {},
                    onDragEnd = {}
                ) { c, dragAmount ->
                    onMove(dragAmount.x, dragAmount.y)
                }
            }
        )

    }
}