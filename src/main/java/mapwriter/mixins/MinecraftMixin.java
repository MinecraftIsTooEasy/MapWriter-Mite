package mapwriter.mixins;

import mapwriter.Mw;
import net.minecraft.Minecraft;
import net.minecraft.WorldClient;
import net.minecraft.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    public WorldClient theWorld;

    @Inject(method = "loadWorld(Lnet/minecraft/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
    private void preLoadWorld(WorldClient par1WorldClient, String par2Str, CallbackInfo ci) {
        if (par1WorldClient == null) {
            Mw.getInstance().onWorldUnload(this.theWorld);
        }
    }

    @Inject(method = "loadWorld(Lnet/minecraft/WorldClient;Ljava/lang/String;)V", at = @At("RETURN"))
    private void postLoadWorld(WorldClient par1WorldClient, String par2Str, CallbackInfo ci) {
        if (par1WorldClient != null) {
            Mw.getInstance().onWorldLoad(par1WorldClient);
        }
    }

    @Inject(method = "launchIntegratedServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/LoadingScreenRenderer;displayProgressMessage(Ljava/lang/String;)V"))
    private void onIntegratedServerLaunch(String par1Str, String par2Str, WorldSettings par3WorldSettings, CallbackInfo ci) {
        Mw.getInstance().onConnectionOpened();
    }
}
