package me.falu.minecraftbutthevideoqualityequalsyourhealth.listener;

import me.falu.minecraftbutthevideoqualityequalsyourhealth.QualityMod;

public interface ResetListener {
    default void register(ResetListener listener) {
        if (!QualityMod.LISTENERS.contains(listener)) {
            QualityMod.LISTENERS.add(listener);
        }
    }

    void minecraftbutthevideoqualityequalsyourhealth$onReset();
}
