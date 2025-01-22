package Command

import Models.ModelShape

//переименовать
class MoveShapeCommand(
    doExecute: () -> Unit,
    doUnexecute: () -> Unit,
    val value: ModelShape
) : FunctionalCommand(
    doExecute,
    doUnexecute
) {
    override fun tryMergeWith(other: ICommand) = when {
        other.javaClass.name == this.javaClass.name
                && other is MoveShapeCommand
                && value.id == other.value.id -> {
            mDoExecute = {
                other.execute()
            }
            true
        }

        else -> false
    }
}
