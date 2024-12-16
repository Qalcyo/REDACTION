package org.polyfrost.redaction

//#if FORGE
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
//#else
//$$ import net.fabricmc.api.ClientModInitializer
//#endif

import org.polyfrost.oneconfig.api.commands.v1.CommandManager
import org.polyfrost.redaction.command.RedactionCommand
import org.polyfrost.redaction.config.RedactionConfig
import org.polyfrost.redaction.features.BlackBar
import org.polyfrost.redaction.features.particles.ParticleManager
import org.polyfrost.redaction.features.ServerManager

//#if FORGE
@Mod(modid = Redaction.ID, version = Redaction.VERSION, name = Redaction.NAME, modLanguageAdapter = "org.polyfrost.oneconfig.utils.v1.forge.KotlinLanguageAdapter")
//#endif
object Redaction
    //#if FABRIC
    //$$ : ClientModInitializer
    //#endif
{

    const val NAME = "@MOD_NAME@"
    const val VERSION = "@MOD_VERSION@"
    const val ID = "@MOD_ID@"

    var overrideHand = false
    var renderingHand = false

    private fun initialize() {
        RedactionConfig.preload()
        CommandManager.registerCommand(RedactionCommand())
        ServerManager.initialize()
    }

    //#if FORGE
    @Mod.EventHandler
    fun onFMLInitialization(event: FMLInitializationEvent) {
        initialize()
        MinecraftForge.EVENT_BUS.register(ParticleManager)
        MinecraftForge.EVENT_BUS.register(ServerManager)
    }

    @Mod.EventHandler
    fun onFMLPost(e: FMLLoadCompleteEvent) {
        BlackBar.initialize()
    }
    //#else
    //$$ override fun onInitializeClient() {
    //$$     initialize()
    //$$ }
    //#endif

}