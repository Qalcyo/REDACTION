package org.polyfrost.redaction.features

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import org.polyfrost.oneconfig.utils.v1.dsl.mc
import org.polyfrost.polyui.color.toJavaColor
import org.polyfrost.redaction.config.RedactionConfig
import org.polyfrost.redaction.config.RedactionConfig.blackbarSlotNumbers
import org.polyfrost.redaction.features.blackbar.HotbarRenderer
import org.polyfrost.redaction.mixin.MinecraftAccessor
import org.polyfrost.redaction.utils.MathUtil
import org.polyfrost.redaction.utils.RenderUtils
import org.polyfrost.universal.UResolution
import java.awt.Color
import kotlin.math.roundToInt

object BlackBar {

    private var cachedHeight = 0
    private var cachedWidth = 0
    private var currentX = 0f
    private var currentY = 0f
    private var lastSlot = 10

    private val texPath = ResourceLocation("textures/gui/widgets.png")
    private var firstTime = true

    @JvmStatic
    fun handleRenderTick(renderer: HotbarRenderer, partialTicks: Float) {
        if (mc.theWorld == null || mc.thePlayer == null) {
            return
        }

        if (cachedHeight != UResolution.scaledHeight || cachedWidth != UResolution.scaledWidth) {
            cachedHeight = UResolution.scaledHeight
            cachedWidth = UResolution.scaledWidth
            currentX = -1f
            currentY = UResolution.scaledHeight - 22f
        }

        val cameraEntity = mc.renderViewEntity as? EntityPlayer ?: return

        // Checks for hide state
        var hiding = mc.currentScreen is GuiChat
        if (hiding) {
            currentY = MathUtil.lerp(currentY, UResolution.scaledHeight + 2f, (mc as MinecraftAccessor).timer.renderPartialTicks / 4)
        } else if (currentY != UResolution.scaledHeight - 21f) {
            currentY = MathUtil.lerp(currentY, UResolution.scaledHeight - 22f, (mc as MinecraftAccessor).timer.renderPartialTicks / 4)
        }

        // Checks for slot change
        if (lastSlot != cameraEntity.inventory.currentItem) {
            if (UResolution.scaledWidth / 2 - 91f + cameraEntity.inventory.currentItem * 20 != currentX) {
                currentX = MathUtil.lerp(
                    currentX,
                    UResolution.scaledWidth / 2 - 91f + cameraEntity.inventory.currentItem * 20,
                    (mc as MinecraftAccessor).timer.renderPartialTicks / 4
                )
            } else {
                lastSlot = cameraEntity.inventory.currentItem
            }
        } else {
            lastSlot = cameraEntity.inventory.currentItem
            currentX = UResolution.scaledWidth / 2 - 91f + cameraEntity.inventory.currentItem * 20
        }

        // Actually render the thing
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        Minecraft.getMinecraft().textureManager.bindTexture(texPath)
        if (firstTime) {
            firstTime = false
            currentX = 0F
            currentY = UResolution.scaledHeight - 22f
            hiding = false
        }

        if (!hiding) {
            if (RedactionConfig.blackbarColor.alpha != 0f) {
                RenderUtils.drawRectEnhanced(0, 0, UResolution.scaledWidth, 22, RedactionConfig.blackbarColor.rgba)
            }

            if (RedactionConfig.blackbarItemColor.alpha != 0f) {
                RenderUtils.drawRectangle(
                    currentX,
                    currentY,
                    22F,
                    22F,
                    RedactionConfig.blackbarItemColor.toJavaColor()
                )
            }
        }

        GlStateManager.enableDepth()
        GlStateManager.enableRescaleNormal()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        RenderHelper.enableGUIStandardItemLighting()

        for (j in 0..8) {
            val textX = UResolution.scaledWidth / 2 - 90 + j * 20 + 2
            val textY = currentY + 3
            renderer.`redaction$renderHotbarItem`(j, textX, textY.roundToInt(), partialTicks, Minecraft.getMinecraft().renderViewEntity as EntityPlayer)

            if (blackbarSlotNumbers) {
                GlStateManager.disableDepth()
                GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0)
                mc.fontRendererObj.drawString(
                    (j + 1).toString(),
                    textX.toFloat(),
                    textY,
                    Color.WHITE.rgb,
                    false
                )

                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
                GlStateManager.enableDepth()
            }
        }

        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
        GlStateManager.disableBlend()
    }

}