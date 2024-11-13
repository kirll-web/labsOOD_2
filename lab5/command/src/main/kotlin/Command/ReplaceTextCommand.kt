package org.command.Command

class ReplaceTextCommand(
    doExecute: () -> Unit,
    doUnexecute: () -> Unit
): FunctionalCommand(
    doExecute,
    doUnexecute
)