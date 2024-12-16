package org.polyfrost.redaction.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.polyfrost.redaction.Redaction;
import org.lwjgl.util.glu.Project;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin_OptiFine {

    @Shadow
    private Minecraft mc;

    @Shadow
    private float farPlaneDistance;

    @Shadow
    protected abstract float getFOVModifier(float partialTicks, boolean useFOVSetting);

    @Dynamic("I HATE OPTIFINE")
    @Inject(method = {"renderHand*"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;loadIdentity()V", shift = At.Shift.AFTER, ordinal = 0, remap = true), remap = false)
    private void modifyFov(float f, int n, boolean bl, boolean bl2, boolean bl3, CallbackInfo ci) {
        Redaction.INSTANCE.setOverrideHand(true);
    }

    @Dynamic("I HATE OPTIFINE")
    @Inject(method = "renderHand*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V", remap = true), remap = false)
    private void setHookTrue(float f, int n, boolean bl, boolean bl2, boolean bl3, CallbackInfo ci) {
        Redaction.INSTANCE.setRenderingHand(true);
    }

    @Dynamic("I HATE OPTIFINE")
    @Inject(method = "renderHand*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;renderItemInFirstPerson(F)V", shift = At.Shift.AFTER, remap = true), remap = false)
    private void setHookFalse(float f, int n, boolean bl, boolean bl2, boolean bl3, CallbackInfo ci) {
        Redaction.INSTANCE.setRenderingHand(false);
    }

    @Dynamic("I HATE OPTIFINE")
    @Inject(method = "renderHand*", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;thirdPersonView:I", opcode = Opcodes.GETFIELD, ordinal = 1, remap = true), remap = false)
    private void redaction$resetFov(float f, int n, boolean bl, boolean bl2, boolean bl3, CallbackInfo ci) {
        redaction$resetFov(f, n);
    }

    @Unique
    private void redaction$resetFov(float partialTicks, int xOffset) {
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float) (-(xOffset * 2 - 1)) * 0.1F, 0.0F, 0.0F);
        }

        Project.gluPerspective(this.getFOVModifier(partialTicks, false), (float) this.mc.displayWidth / (float) this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        if (this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float) (xOffset * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }
    }
}
