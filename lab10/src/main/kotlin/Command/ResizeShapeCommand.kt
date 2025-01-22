package Command

import Models.ModelShape

class ResizeShapeCommand(
    doExecute: () -> Unit,
    doUnexecute: () -> Unit,
    val value: ModelShape
) : FunctionalCommand(
    doExecute,
    doUnexecute
) {
    override fun tryMergeWith(other: ICommand) = when {
        other.javaClass.name == this.javaClass.name
                && other is ResizeShapeCommand
                && value.id == other.value.id -> {
            mDoExecute = {
                other.execute()
            }
            true
        }

        else -> false
    }
}
