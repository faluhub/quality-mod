package me.falu.minecraftbutthevideoqualityequalsyourhealth.mixin;

import me.falu.minecraftbutthevideoqualityequalsyourhealth.AudioDistorter;
import net.minecraft.client.sound.Source;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Source.class)
public class SourceMixin {
    @Shadow @Final private int pointer;

    @Inject(method = "play", at = @At("HEAD"))
    private void tickDistortion(CallbackInfo ci) {
        AudioDistorter.INSTANCE.applyValue(this.pointer);
    }
}
