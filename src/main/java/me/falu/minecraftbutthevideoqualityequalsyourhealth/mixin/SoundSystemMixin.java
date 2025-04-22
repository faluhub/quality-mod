package me.falu.minecraftbutthevideoqualityequalsyourhealth.mixin;

import me.falu.minecraftbutthevideoqualityequalsyourhealth.AudioDistorter;
import me.falu.minecraftbutthevideoqualityequalsyourhealth.mixin.access.SourceAccessor;
import net.minecraft.client.sound.Channel;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@SuppressWarnings("unchecked")
@Mixin(SoundSystem.class)
public class SoundSystemMixin {
    @Shadow @Final private Map<SoundInstance, Channel.SourceManager> sources;

    @Unique
    private void addFilterToManager(Channel.SourceManager manager) {
        manager.run(source -> AudioDistorter.INSTANCE.applyValue(((SourceAccessor) source).getPointer()));
    }

    @Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0))
    private <V> V addEffects1(Map<?, Channel.SourceManager> instance, Object o) {
        Channel.SourceManager manager = instance.get(o);
        this.addFilterToManager(manager);
        return (V) manager;
    }

    @Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Ljava/util/Map$Entry;getValue()Ljava/lang/Object;", ordinal = 0))
    private <V> V addEffects2(Map.Entry<SoundInstance, Channel.SourceManager> instance) {
        Channel.SourceManager manager = instance.getValue();
        this.addFilterToManager(manager);
        return (V) manager;
    }

    @Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundSystem;play(Lnet/minecraft/client/sound/SoundInstance;)V"))
    private void addEffects3(SoundSystem instance, SoundInstance sound) {
        instance.play(sound);
        Channel.SourceManager manager = this.sources.get(sound);
        if (manager != null) {
            this.addFilterToManager(manager);
        }
    }
}
