package me.falu.minecraftbutthevideoqualityequalsyourhealth.mixin;

import me.falu.minecraftbutthevideoqualityequalsyourhealth.QualityMod;
import me.falu.minecraftbutthevideoqualityequalsyourhealth.mixin.access.PostEffectProcessorAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.Uniform;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    private static final Random RANDOM = new Random();
    private static final Identifier SHADER = new Identifier("shaders/post/shader.json");
    private static float HUE = RANDOM.nextFloat();
    private float lastHealth = -1.0F;
    private int lastFpsValue = -1;
    @Shadow @Nullable PostEffectProcessor postProcessor;
    @Shadow abstract void loadPostProcessor(Identifier id);
    @Shadow @Final MinecraftClient client;
    @Shadow private boolean postProcessorEnabled;

    private List<Uniform> getUniform(String uniformName) {
        if (this.postProcessor != null) {
            if (this.postProcessor.getName().equals(SHADER.toString())) {
                List<Uniform> list = new ArrayList<>();
                List<PostEffectPass> passes = ((PostEffectProcessorAccessor) this.postProcessor).getPasses();
                for (PostEffectPass pass : passes) {
                    Uniform uniform = pass.getProgram().getUniformByName(uniformName);
                    if (uniform != null) {
                        list.add(uniform);
                    }
                }
                return list;
            }
            this.postProcessor.close();
            this.postProcessor = null;
        }
        this.loadPostProcessor(SHADER);
        return this.getUniform(uniformName);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void addShader(CallbackInfo ci) {
        this.postProcessorEnabled = true;
        if (this.client.player == null) { return; }
        for (Uniform uniform : this.getUniform("Hue")) {
            uniform.set(HUE);
        }
        float health = this.client.player.getHealth();
        if (health <= 0.0F) {
            if (QualityMod.GRAYSCALE_DEATH) {
                for (Uniform uniform : this.getUniform("Saturation")) {
                    uniform.set(0.0F);
                }
            } else {
                for (Uniform uniform : this.getUniform("Saturation")) {
                    uniform.set(1.0F);
                }
            }
            for (Uniform uniform : this.getUniform( "Quality")) {
                uniform.set(8.0F);
            }
            this.lastHealth = -1.0F;
            this.client.options.getMaxFps().setValue(QualityMod.DEFAULT_FPS_VAL);
            this.lastFpsValue = QualityMod.DEFAULT_FPS_VAL;
            HUE = RANDOM.nextFloat();
            return;
        }
        float maxHealth = this.client.player.getMaxHealth();
        if (this.lastHealth == -1.0F || this.lastHealth != health) {
            QualityMod.setMicLevels();
            float value = (float) Math.pow((maxHealth - health) / 10.0D, QualityMod.POWER) * QualityMod.INTENSITY_FACTOR;
            value = Math.max(1.0F, value);
            int fpsValue = Math.max(Math.max(QualityMod.LOWEST_FPS_VAL, 10), (int) (QualityMod.DEFAULT_FPS_VAL / maxHealth * health));
            for (Uniform uniform : this.getUniform("Saturation")) {
                uniform.set(value);
            }
            value = 8.0F / value * 2.0F;
            for (Uniform uniform : this.getUniform("Quality")) {
                uniform.set(value);
            }
            if (this.lastFpsValue == -1 || this.client.options.getMaxFps().getValue() != this.lastFpsValue) {
                this.client.options.getMaxFps().setValue(fpsValue);
            }
            this.lastFpsValue = fpsValue;
        }
        this.lastHealth = health;
    }

    @Inject(method = "onCameraEntitySet", at = @At("HEAD"), cancellable = true)
    private void removePostShaders(CallbackInfo ci) {
        ci.cancel();
    }
}
