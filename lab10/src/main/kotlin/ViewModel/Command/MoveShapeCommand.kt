package Command

class MoveShapeCommand(
    doExecute: () -> Unit,
    doUnexecute: () -> Unit
): FunctionalCommand(
    doExecute,
    doUnexecute
)
