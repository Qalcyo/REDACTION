package org.polyfrost.redaction.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;
import org.polyfrost.redaction.config.RedactionConfig;
import org.polyfrost.redaction.features.BlackBar;
import org.polyfrost.redaction.features.blackbar.HotbarRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiIngame.class)
public abstract class GuiIngameMixin implements HotbarRenderer {

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
                BlackBar.handleRenderTick(this, partialTicks);
            }
        }
    }

    public void redaction$renderHotbarItem(int index, int x, int y, float partialTicks, @NotNull EntityPlayer player) {
        this.renderHotbarItem(index, x, y, partialTicks, player);
    }
}
