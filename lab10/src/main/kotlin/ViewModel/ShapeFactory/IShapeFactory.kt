package ViewModel.ShapeFactory

import Models.ModelShape

interface IModelShapeFactory {
    fun createShape(type: ShapeType, url: String? = null): ModelShape
}