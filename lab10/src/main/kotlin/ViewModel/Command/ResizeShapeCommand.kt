package Command

import Models.ModelShape
import Models.Models

class ResizeShapeCommand(
    doExecute: () -> Unit,
    doUnexecute: () -> Unit
): FunctionalCommand(
    doExecute,
    doUnexecute
)
