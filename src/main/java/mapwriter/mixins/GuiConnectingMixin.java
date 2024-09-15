package mapwriter.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import mapwriter.Mw;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public class GuiConnectingMixin {
    @Inject(method = "<init>(Lnet/minecraft/GuiScreen;Lnet/minecraft/Minecraft;Lnet/minecraft/ServerData;)V", at = @At("RETURN"))
    private void connectionOpened(GuiScreen par1GuiScreen, Minecraft par2Minecraft, ServerData par3ServerData, CallbackInfo ci, @Local ServerAddress var4) {
        Mw.getInstance().onConnectionOpened(par3ServerData.serverName, var4.getPort());
    }
}
