package mapwriter.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mapwriter.Mw;
import net.minecraft.Chunk;
import net.minecraft.ChunkProviderClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkProviderClient.class)
public class ChunkProviderClientMixin {
    @WrapOperation(method = "unloadChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/Chunk;onChunkUnload()V"))
    private void onChunkUnload(Chunk instance, Operation<Void> original) {
        original.call(instance);
        Mw.getInstance().onChunkUnload(instance);
    }

    @Inject(method = "loadChunk", at = @At("RETURN"))
    private void onChunkLoad(int par1, int par2, CallbackInfoReturnable<Chunk> cir) {
        Mw.getInstance().onChunkLoad(cir.getReturnValue());
    }

}
