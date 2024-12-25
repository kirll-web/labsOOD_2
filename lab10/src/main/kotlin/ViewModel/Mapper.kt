package ViewModel

import Models.ModelShape
import ViewModel.Canvas.CanvasState
import ViewModel.Figures.*
import ViewModel.Styles.FillStyle
import ViewModel.Styles.StrokeStyle
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import java.util.*

class Mapper {
    fun mapToCanvasState(
        oldState: MutableStateFlow<CanvasState>,
        selectedShapedId: MutableState<String?>,
        shapes: Map<String, ModelShape>? = null
    ): CanvasState {
        var isHasSelected = false
        val canvasState = oldState.value.copy(
            shapes = shapes?.map { item ->
                val isSelect = item.key == selectedShapedId.value
                if (isSelect) isHasSelected = true
                item.key to mapToShape(item.value, isSelect)
            }?.toMap()?.toMutableMap().apply {
                selectedShapedId.value?.let {
                    val shape = shapes?.get(it)
                    shape?.let {
                        if (isHasSelected) {
                            val id = UUID.randomUUID().toString()
                            this?.set(
                                id, TargetShape(
                                    id,
                                    RectFloat(
                                        shape.x,
                                        shape.y,
                                        shape.width,
                                        shape.height
                                    ),
                                    StrokeStyle(0u, null),
                                    FillStyle(null),
                                    false
                                )
                            )
                        }
                    }
                }
            } ?: oldState.value.shapes.toMutableMap().apply {
                val prevTargetShape = oldState.value.shapes.values.findLast {
                    it is TargetShape
                }

                selectedShapedId.value?.let {
                    val shape = oldState.value.shapes.get(it)
                    shape?.let {
                        val id = UUID.randomUUID().toString()
                        prevTargetShape?.let { prevTargetShape -> remove(prevTargetShape.id) }
                        set(
                            id, TargetShape(
                                id,
                                RectFloat(
                                    shape.getFrame().left,
                                    shape.getFrame().top,
                                    shape.getFrame().width,
                                    shape.getFrame().height
                                ),
                                StrokeStyle(0u, null),
                                FillStyle(null),
                                false
                            )
                        )
                    }
                }
            }
        )
        return canvasState
    }

    fun mapToModelShape(shape: IShape) = when (shape) {
        is Rectangle -> ModelShape.Rectangle(
            shape.id,
            shape.getFrame().left,
            shape.getFrame().top,
            shape.getFrame().width,
            shape.getFrame().height,
            shape.getFillStyle().getColor()?.let { Color(it).value } ?: Color.Black.value
        )

        is Ellipse -> ModelShape.Ellipse(
            shape.id,
            shape.getFrame().left,
            shape.getFrame().top,
            shape.getFrame().width,
            shape.getFrame().height,
            shape.getFillStyle().getColor()?.let { Color(it).value } ?: Color.Black.value
        )

        is Triangle -> ModelShape.Triangle(
            shape.id,
            shape.getFrame().left,
            shape.getFrame().top,
            shape.getFrame().width,
            shape.getFrame().height,
            shape.getFillStyle().getColor()?.let { Color(it).value } ?: Color.Black.value
        )

        is Image -> ModelShape.Image(
            shape.id,
            shape.getFrame().left,
            shape.getFrame().top,
            shape.getFrame().width,
            shape.getFrame().height,
            shape.getFillStyle().getColor()?.let { Color(it).value } ?: (0x00000000).toULong(),
            imgURL = shape.imgUrl
        )

        is TargetShape -> null

        else -> throw IllegalArgumentException("Unknowed figure")
    }

    fun mapToShape(
        shape: ModelShape,
        isSelect: Boolean? = null
    ) = with(shape) {
        when (shape) {
            is ModelShape.Rectangle -> Rectangle(
                this.id,
                RectFloat(
                    x,
                    y,
                    width,
                    height
                ),
                StrokeStyle(
                    0u,
                    null
                ),
                FillStyle(
                    color
                ),
                isSelect ?: false
            )

            is ModelShape.Ellipse -> Ellipse(
                this.id,
                RectFloat(
                    x,
                    y,
                    width,
                    height
                ),
                StrokeStyle(
                    0u,
                    null
                ),
                FillStyle(
                    color
                ),
                isSelect ?: false
            )

            is ModelShape.Triangle -> Triangle(
                this.id,
                TrianglePoints(
                    leftBottom = Point(
                        x, y + height
                    ),
                    top = Point(
                        x + width / 2, y
                    ),
                    rightBottom = Point(
                        x + width,
                        y + height
                    )
                ),
                StrokeStyle(
                    0u,
                    null
                ),
                FillStyle(
                    color
                ),
                isSelect ?: false
            )

            is ModelShape.Image -> {
                val file = File(shape.url)
                val imageBitmap: ImageBitmap = loadImageBitmap(file.inputStream())
                Image(
                    this.id,
                    RectFloat(
                        x,
                        y,
                        width,
                        height
                    ),
                    StrokeStyle(
                        0u,
                        null
                    ),
                    FillStyle(
                        color
                    ),
                    isSelect ?: false,
                    imgUrl = shape.url,
                    imageBitmap = imageBitmap
                )
            }
        }
    }
}