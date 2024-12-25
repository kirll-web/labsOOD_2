package ViewModel.ShapeFactory

import Models.ModelShape
import androidx.compose.ui.graphics.Color
import java.util.*

class ShapeFactory(
    private var windowWidth: Float,
    private var windowHeight: Float,
    private val defaultWidthShape: Float,
    private val defaultHeightShape: Float,
) : IModelShapeFactory {
    override fun createShape(type: ShapeType, url: String?): ModelShape =
        when (type) {
            ShapeType.IMAGE -> {
                url?.let {
                    createImage(url)
                } ?: throw Exception("Url required for image")
            }

            ShapeType.RECTANGLE -> {
                createRectangle()
            }

            ShapeType.TRIANGLE -> {
                createTriangle()
            }

            ShapeType.ELLIPSE -> {
                createEllipse()
            }
        }


    private fun createRectangle() =
        ModelShape.Rectangle(
            UUID.randomUUID().toString(),
            (windowWidth / 2 - defaultWidthShape / 2),
            (windowHeight / 2 - defaultHeightShape / 2),
            defaultWidthShape,
            defaultHeightShape,
            Color.Black.value
        )


    private fun createEllipse() =
        ModelShape.Ellipse(
            UUID.randomUUID().toString(),
            (windowWidth / 2 - defaultWidthShape / 2),
            (windowHeight / 2 - defaultHeightShape / 2),
            defaultWidthShape,
            defaultHeightShape,
            Color.Red.value
        )


    private fun createTriangle() =
        ModelShape.Triangle(
            UUID.randomUUID().toString(),
            (windowWidth / 2 - defaultWidthShape / 2),
            (windowHeight / 2 - defaultHeightShape / 2),
            defaultWidthShape,
            defaultHeightShape,
            Color.Green.value
        )

    private fun createImage(url: String) = ModelShape.Image(
        UUID.randomUUID().toString(),
        (windowWidth / 2 - defaultWidthShape / 2),
        (windowHeight / 2 - defaultHeightShape / 2),
        defaultWidthShape,
        defaultHeightShape,
        Color.Red.value,
        url
    )
}