package mapwriter.mixins;

import mapwriter.Mw;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetClientHandler.class)
public abstract class NetClientHandlerMixin extends NetHandler {

    @Inject(method = "handleLogin", at = @At("RETURN"))
    private void onLogin(Packet1Login packet1Login, CallbackInfo ci) {
        Mw.getInstance().onClientLoggedIn(packet1Login);
    }

    @Inject(method = "quitWithPacket", at = @At(value = "INVOKE", target = "Lnet/minecraft/INetworkManager;addToSendQueue(Lnet/minecraft/Packet;)V"))
    private void onClientQuit(Packet par1Packet, CallbackInfo ci) {
        Mw.getInstance().onConnectionClosed();
    }
}
