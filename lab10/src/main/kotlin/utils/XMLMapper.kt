package utils

import Models.ModelShape

interface IXMLMapper <T, K> {
    fun mapToXML(shape: T): K
    fun mapFromXML(shape: K): T
}

class XMLMapper: IXMLMapper<ModelShape, ModelShapeXML> {
    override fun mapToXML(shape: ModelShape) = when (shape) {
        is ModelShape.Image -> {
            ModelShapeXML(
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
        else -> ModelShapeXML(
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

    override fun mapFromXML(shape: ModelShapeXML) = when (shape.type) {
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