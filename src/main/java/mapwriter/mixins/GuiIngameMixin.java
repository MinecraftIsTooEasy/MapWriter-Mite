package mapwriter.mixins;

import mapwriter.Mw;
import net.minecraft.GuiIngame;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class GuiIngameMixin {
    @Shadow
    @Final
    private Minecraft mc;

    @Inject(
            method = {"renderGameOverlay(FZII)V"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/Minecraft;inDevMode()Z",
                    shift = At.Shift.BEFORE
            )}
    )
    private void renderMiniMap(float par1, boolean par2, int par3, int par4, CallbackInfo ci) {
        if (this.mc.gameSettings.gui_mode == 0 && !this.mc.gameSettings.showDebugInfo)
            Mw.getInstance().onRenderTick();
    }
}
