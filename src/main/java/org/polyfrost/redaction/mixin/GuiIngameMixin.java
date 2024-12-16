package org.polyfrost.redaction.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import org.polyfrost.redaction.config.RedactionConfig;
import org.polyfrost.redaction.features.BlackBar;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiIngame.class)
public abstract class GuiIngameMixin {

    @Shadow
    @Final
    protected Minecraft mc;

    @Shadow
    protected abstract void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer player);

    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    private void cancel(ScaledResolution res, float partialTicks, CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getBlackbar()) {
            ci.cancel();
            if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
                BlackBar.INSTANCE.render();
                GlStateManager.enableDepth();
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                RenderHelper.enableGUIStandardItemLighting();
                for (int j = 0; j < 9; ++j) {
                    int k = res.getScaledWidth() / 2 - 90 + j * 20 + 2;
                    int l = res.getScaledHeight() - 16 - 3;
                    renderHotbarItem(j, k, l, partialTicks, (EntityPlayer) Minecraft.getMinecraft().getRenderViewEntity());

                    if (RedactionConfig.INSTANCE.getBlackbarSlotNumbers()){
                        GlStateManager.disableDepth();
                        GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
                        this.mc.fontRendererObj.drawString(
                                String.valueOf(j + 1),
                                k,
                                l,
                                Color.WHITE.getRGB(),
                                false
                        );

                        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                        GlStateManager.enableDepth();
                    }
                }

                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
            }
        }
    }

}
