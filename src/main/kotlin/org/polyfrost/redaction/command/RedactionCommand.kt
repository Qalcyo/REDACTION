package org.polyfrost.redaction.command

import org.polyfrost.oneconfig.api.commands.v1.factories.annotated.Command
import org.polyfrost.redaction.config.RedactionConfig
import org.polyfrost.utils.v1.dsl.openUI

@Command("redaction")
class RedactionCommand {
    @Command
    fun main() {
        RedactionConfig.openUI()
    }
}