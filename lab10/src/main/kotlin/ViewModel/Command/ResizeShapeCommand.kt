package Command

class ResizeShapeCommand(
    doExecute: () -> Unit,
    doUnexecute: () -> Unit
): FunctionalCommand(
    doExecute,
    doUnexecute
)
