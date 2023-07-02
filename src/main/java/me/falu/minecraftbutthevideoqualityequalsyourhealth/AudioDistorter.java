package me.falu.minecraftbutthevideoqualityequalsyourhealth;

import me.falu.minecraftbutthevideoqualityequalsyourhealth.mixin.access.SoundManagerAccessor;
import me.falu.minecraftbutthevideoqualityequalsyourhealth.mixin.access.SoundSystemAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.AlUtil;
import net.minecraft.client.sound.SoundSystem;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;

/**
 * Mostly based off of <a href="https://github.com/vic4games/weaponlib/blob/c391fccbc0e0e7e47df150504541eae322e98cd3/src/main/java/com/vicmatskiv/weaponlib/compatibility/CoreSoundInterceptor.java">this class</a>.
 * Also used <a href="https://openal-soft.org/misc-downloads/Effects%20Extension%20Guide.pdf">this document</a> as a reference.
 */
public class AudioDistorter {
    public static final AudioDistorter INSTANCE = new AudioDistorter();
    private static int SLOT = -1;
    private static int DISTORTION = -1;
    private final MinecraftClient client = MinecraftClient.getInstance();
    private int errorIndex = 0;

    public void applyValue(int source) {
        if (!QualityMod.DEEPFRY_AUDIO) { return; }

        if (this.client.player == null || this.client.isPaused()) { return; }

        SoundSystem system = ((SoundManagerAccessor) this.client.getSoundManager()).getSoundSystem();
        if (system == null || !((SoundSystemAccessor) system).getStarted()) { return; }
        this.errorIndex = 0;

        if (SLOT == -1) {
            SLOT = EXTEfx.alGenAuxiliaryEffectSlots();
            this.checkError();
            EXTEfx.alAuxiliaryEffectSloti(SLOT, EXTEfx.AL_EFFECTSLOT_AUXILIARY_SEND_AUTO, AL10.AL_TRUE);
            this.checkError();
        }
        if (DISTORTION == -1) {
            DISTORTION = EXTEfx.alGenEffects();
            this.checkError();
            EXTEfx.alEffecti(DISTORTION, EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_DISTORTION);
            this.checkError();
        }
        if (SLOT == -1 || DISTORTION == -1) { return; }

        float x = this.client.player.getHealth();
        double y;
        if (x <= 1.0F && x > 0.0F) { y = 7.5D; }
        else if (x <= 0.0F || x / this.client.player.getMaxHealth() * 100.0D > QualityMod.HEALTH_PERCENTAGE_UNTIL_EARRAPE) { y = 0.0D; }
        else { y = 1.0 + 5.0D - x / 2.0D / 10.0D * 5.0D; }
        double value = y / 10.0D;
        value = Math.min(1.0D, Math.max(value, 0.01D));

        EXTEfx.alEffectf(DISTORTION, EXTEfx.AL_DISTORTION_EDGE, (float) value);
        this.checkError();
        EXTEfx.alEffectf(DISTORTION, EXTEfx.AL_DISTORTION_GAIN, (float) value);
        this.checkError();
        EXTEfx.alAuxiliaryEffectSloti(SLOT, EXTEfx.AL_EFFECTSLOT_EFFECT, DISTORTION);
        this.checkError();
        AL11.alSource3i(source, EXTEfx.AL_AUXILIARY_SEND_FILTER, SLOT, 0, AL11.AL_NONE);
        this.checkError();
    }

    private void checkError() {
        AlUtil.checkErrors(String.valueOf(this.errorIndex));
        this.errorIndex++;
    }
}
