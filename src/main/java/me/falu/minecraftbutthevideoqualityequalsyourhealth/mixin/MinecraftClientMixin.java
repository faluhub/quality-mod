package me.falu.minecraftbutthevideoqualityequalsyourhealth.mixin;

import me.falu.minecraftbutthevideoqualityequalsyourhealth.QualityMod;
import me.falu.minecraftbutthevideoqualityequalsyourhealth.listener.ResetListener;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "reloadResources()Ljava/util/concurrent/CompletableFuture;", at = @At("TAIL"))
    private void updateConfig(CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        if (!QualityMod.CONTROLLER_CONNECTED) {
            QualityMod.CONTROLLER.connect();
        }
        QualityMod.setConfigValues();
        QualityMod.LISTENERS.forEach(ResetListener::minecraftbutthevideoqualityequalsyourhealth$onReset);
    }
}
