package me.falu.minecraftbutthevideoqualityequalsyourhealth.mixin.access;

import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SoundSystem.class)
public interface SoundSystemAccessor {
    @Accessor("started") boolean getStarted();
}
