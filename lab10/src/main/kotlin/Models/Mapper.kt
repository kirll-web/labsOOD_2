package ViewModel

import Models.ModelShape
import Models.ModelShapeJson
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

interface IJsonMapper <T, K> {
    fun mapToJson(shape: T): K
    fun mapFromJson(shape: K): T
}

class JsonMapper: IJsonMapper<ModelShape, ModelShapeJson> {
    override fun mapToJson(shape: ModelShape) = when (shape) {
        is ModelShape.Image -> {
            ModelShapeJson(
                id = shape.id,
                type = shape::class.simpleName.toString(),
                x = shape.x,
                y = shape.y,
                width = shape.width,
                height = shape.height,
                color = shape.color,
                shape.url //FIXME MOCK
            )
        }
        else -> ModelShapeJson(
            id = shape.id,
            type = shape::class.simpleName.toString(),
            x = shape.x,
            y = shape.y,
            width = shape.width,
            height = shape.height,
            color = shape.color,
            null //FIXME MOCK
        )
    }
    override fun mapFromJson(shape: ModelShapeJson) = when (shape.type) {
        ModelShape.Ellipse::class.simpleName -> ModelShape.Ellipse(
            id = shape.id,
            x = shape.x,
            y = shape.y,
            width = shape.width,
            height = shape.height,
            color = shape.color,
        )
        ModelShape.Triangle::class.simpleName -> ModelShape.Triangle(
            id = shape.id,
            x = shape.x,
            y = shape.y,
            width = shape.width,
            height = shape.height,
            color = shape.color,
        )

        ModelShape.Image::class.simpleName -> ModelShape.Image(
            id = shape.id,
            x = shape.x,
            y = shape.y,
            width = shape.width,
            height = shape.height,
            color = shape.color,
            imgURL = shape.url ?: ""
        )

        else -> ModelShape.Rectangle(
            id = shape.id,
            x = shape.x,
            y = shape.y,
            width = shape.width,
            height = shape.height,
            color = shape.color,
        )
    }
}