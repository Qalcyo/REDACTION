package org.polyfrost.redaction.features.particles

//#if FORGE
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
//#else
//$$ // TODO
//#endif

import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.inventory.GuiContainer
import org.polyfrost.redaction.config.RedactionConfig

object ParticleManager {

    var hasChanged = false
    private val particleGenerator = ParticleGenerator()

    fun initialize() {
        //#if FORGE
        MinecraftForge.EVENT_BUS.register(this)
        //#else
        //$$ // TODO
        //#endif
    }

    //#if FORGE
    @SubscribeEvent
    fun render(event: GuiScreenEvent.DrawScreenEvent.Pre) {
        renderParticles(event.gui)
    }
    //#endif

    private fun renderParticles(screen: GuiScreen) {
        if (screen is GuiContainer && RedactionConfig.addSnow) {
            particleGenerator.draw(screen.width, screen.height)
        }
    }

}