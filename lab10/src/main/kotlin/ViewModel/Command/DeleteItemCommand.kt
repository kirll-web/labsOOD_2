package Command

import Models.ModelShape
import kotlinx.coroutines.flow.MutableStateFlow

class DeleteItemCommand(
    private var shapes: MutableStateFlow<Map<String, ModelShape>>,
    private val value: ModelShape,
    private val position: Int? = null
) : AbstractCommand() {
    override fun doExecute() {
        shapes.value[value.id]?.let {
            shapes.value = shapes.value.minus(value.id)
        }
    }
    override fun doUnexecute() {
        when {
            position == null -> {
                shapes.value = shapes.value.plus(value.id to value)
            }

            else ->  {
                //mutable map найти в документации котлина в каком порядке хранятся фигуры
                val newShapes = mutableMapOf<String, ModelShape>()
                var index = 0
                shapes.value.forEach {
                    if (position == index) {
                        newShapes[value.id] = value
                    }
                    newShapes[it.key] = it.value
                    index += 1
                }
                if (position >= shapes.value.values.size) {
                    newShapes[value.id] = value
                }

                shapes.value = newShapes
            }
        }
    }
}

