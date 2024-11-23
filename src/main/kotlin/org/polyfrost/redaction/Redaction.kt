package org.polyfrost.redaction

import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import org.polyfrost.oneconfig.api.commands.v1.CommandManager
import org.polyfrost.redaction.command.RedactionCommand
import org.polyfrost.redaction.config.RedactionConfig
import org.polyfrost.redaction.features.BlackBar
import org.polyfrost.redaction.features.ParticleManager
import org.polyfrost.redaction.features.ServerManager

@Mod(
    name = Redaction.NAME,
    modid = Redaction.ID,
    version = Redaction.VERSION,
    modLanguageAdapter = "org.polyfrost.oneconfig.utils.v1.forge.KotlinLanguageAdapter"
)
object Redaction {

    const val NAME = "@MOD_NAME@"
    const val VERSION = "@MOD_VERSION@"
    const val ID = "@MOD_ID@"
    var overrideHand = false
    var renderingHand = false

    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent) {
        RedactionConfig.preload()
        CommandManager.registerCommand(RedactionCommand())
        EVENT_BUS.register(ParticleManager)
        EVENT_BUS.register(ServerManager)
        ServerManager.initialize()
    }

    @Mod.EventHandler
    fun onFMLPost(e: FMLLoadCompleteEvent) {
        BlackBar.initialize()
    }

}