package org.polyfrost.redaction.mixin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.polyfrost.redaction.Redaction;
import org.polyfrost.redaction.config.RedactionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class RenderItemMixin {

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderModel(Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V"
            )
    )
    private void beforeModelRender(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getDisableHandLighting() && Redaction.INSTANCE.getRenderingHand()) {
            GlStateManager.disableLighting();
        }
    }

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderModel(Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER
            )
    )
    private void afterModelRender(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getDisableHandLighting() && Redaction.INSTANCE.getRenderingHand()) {
            GlStateManager.enableLighting();
        }
    }

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer;renderByItem(Lnet/minecraft/item/ItemStack;)V"
            )
    )
    private void beforeTileModelRender(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getDisableHandLighting() && Redaction.INSTANCE.getRenderingHand()) {
            GlStateManager.disableLighting();
        }
    }

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer;renderByItem(Lnet/minecraft/item/ItemStack;)V", shift = At.Shift.AFTER
            )
    )
    private void afterTileModelRender(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getDisableHandLighting() && Redaction.INSTANCE.getRenderingHand()) {
            GlStateManager.enableLighting();
        }
    }

}
