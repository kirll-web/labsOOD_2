package org.command.Command

class SetTitleCommand(
    doExecute: () -> Unit,
    doUnexecute: () -> Unit
): FunctionalCommand(
    doExecute,
    doUnexecute
)