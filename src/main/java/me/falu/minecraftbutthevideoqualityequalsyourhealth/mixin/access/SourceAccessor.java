package me.falu.minecraftbutthevideoqualityequalsyourhealth.mixin.access;

import net.minecraft.client.sound.Source;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Source.class)
public interface SourceAccessor {
    @Accessor("pointer") int getPointer();
}
