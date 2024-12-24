package org.polyfrost.redaction.mixin;

//#if FORGE
import net.minecraft.client.gui.*;
//#else
//$$ import net.minecraft.client.gui.screen.DirectConnectScreen;
//$$ import net.minecraft.client.gui.screen.Screen;
//$$ import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
//$$ import net.minecraft.client.gui.widget.ServerEntry;
//$$ import net.minecraft.client.gui.widget.TextFieldWidget;
//#endif

import net.minecraft.client.multiplayer.ServerData;
import org.polyfrost.redaction.config.RedactionConfig;
import org.polyfrost.redaction.features.ServerManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenServerList.class)
public class GuiScreenServerListMixin extends GuiScreen {

    @Shadow @Final
    private GuiScreen field_146303_a;

    @Shadow
    private GuiTextField field_146302_g;

    @Unique
    private ServerListEntryNormal redaction$serverPreview;

    @Inject(method = "initGui", at = @At("TAIL"))
    private void redaction$initServerPreview(CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getServerPreview()) {
            this.redaction$serverPreview = new ServerListEntryNormal(((GuiMultiplayer) this.field_146303_a), new ServerData("", "", false));
        }
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiTextField;drawTextBox()V"))
    private void redaction$drawServerPreview(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getServerPreview() && redaction$serverPreview != null) {
            ServerData previewData = this.redaction$serverPreview.getServerData();
            if (!previewData.serverIP.equals(this.field_146302_g.getText())) {
                previewData.serverIP = this.field_146302_g.getText();
                previewData.serverName = ServerManager.INSTANCE.getServerName(this.field_146302_g.getText());
                previewData.field_78841_f = false;
            }

            this.redaction$serverPreview.drawEntry(
                    0,
                    this.width / 2 - 100,
                    30,
                    200,
                    35,
                    mouseX,
                    mouseY,
                    false
                    //#if MC >= 1.12.2
                    //$$ , partialTicks
                    //#endif
            );
        }
    }

}
