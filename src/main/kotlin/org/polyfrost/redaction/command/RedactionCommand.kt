package org.polyfrost.redaction.command

import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command
import org.polyfrost.oneconfig.utils.v1.dsl.openUI
import org.polyfrost.redaction.config.RedactionConfig

@Command("redaction")
class RedactionCommand {
    @Command
    fun main() {
        RedactionConfig.openUI()
    }
}