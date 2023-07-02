package me.falu.minecraftbutthevideoqualityequalsyourhealth;

import io.obswebsocket.community.client.OBSRemoteController;
import io.obswebsocket.community.client.OBSRemoteControllerBuilder;
import me.falu.minecraftbutthevideoqualityequalsyourhealth.listener.ResetListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.*;

import java.util.ArrayList;
import java.util.List;

public class QualityMod implements ClientModInitializer {
    public static final ModContainer MOD_CONTAINER = FabricLoader.getInstance().getModContainer("minecraftbutthevideoqualityequalsyourhealth").orElseThrow(RuntimeException::new);
    public static final String MOD_NAME = MOD_CONTAINER.getMetadata().getName();
    public static final String MOD_VERSION = String.valueOf(MOD_CONTAINER.getMetadata().getVersion());
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static OBSRemoteController CONTROLLER;
    public static boolean CONTROLLER_CONNECTED = false;
    public static final List<ResetListener> LISTENERS = new ArrayList<>();

    public static int POWER;
    public static int OFFSET;
    public static int LOWEST_FPS_VAL;
    public static int DEFAULT_FPS_VAL;
    public static int WEBSOCKET_PORT;
    public static float HEALTH_PERCENTAGE_UNTIL_EARRAPE;
    public static float INTENSITY_FACTOR;
    public static boolean DEEPFRY_AUDIO;
    public static boolean GRAYSCALE_DEATH;
    public static String GOOD_MIC_SOURCE_NAME;
    public static String BAD_MIC_SOURCE_NAME;
    public static String WEBSOCKET_PASSWORD;

    static { setConfigValues(); }

    public static void setConfigValues() {
        POWER = ConfigHandler.getIntValue("power", 2);
        OFFSET = ConfigHandler.getIntValue("offset", 0);
        LOWEST_FPS_VAL = ConfigHandler.getIntValue("lowest_fps_val", 10);
        DEFAULT_FPS_VAL = ConfigHandler.getIntValue("default_fps_val", 120);
        WEBSOCKET_PORT = ConfigHandler.getIntValue("websocket_port", 4444);
        HEALTH_PERCENTAGE_UNTIL_EARRAPE = ConfigHandler.getFloatValue("health_percentage_until_earrape", 85.0F);
        INTENSITY_FACTOR = ConfigHandler.getFloatValue("intensity_factor", 12.5F);
        DEEPFRY_AUDIO = ConfigHandler.getBooleanValue("deepfry_audio", true);
        GRAYSCALE_DEATH = ConfigHandler.getBooleanValue("grayscale_death", true);
        GOOD_MIC_SOURCE_NAME = ConfigHandler.getStringValue("good_mic_source_name", "");
        BAD_MIC_SOURCE_NAME = ConfigHandler.getStringValue("bad_mic_source_name", "");
        WEBSOCKET_PASSWORD = ConfigHandler.getStringValue("websocket_password", "");
    }

    public static void log(Object msg) {
        LOGGER.log(Level.INFO, msg);
    }

    public static void setMicLevels() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!CONTROLLER_CONNECTED || client.player == null) { return; }
        float health = client.player.getHealth();
        float threshold = 10.0F;
        CONTROLLER.setInputMute(GOOD_MIC_SOURCE_NAME, health <= threshold, resp -> {});
        CONTROLLER.setInputMute(BAD_MIC_SOURCE_NAME, health > threshold, resp -> {});
    }

    @Override
    public void onInitializeClient() {
        log("Using " + MOD_NAME + " v" + MOD_VERSION);
        CONTROLLER = new OBSRemoteControllerBuilder()
                .host("localhost")
                .port(WEBSOCKET_PORT)
                .password(WEBSOCKET_PASSWORD.equals("") ? null : WEBSOCKET_PASSWORD)
                .connectionTimeout(3)
                .lifecycle()
                    .onReady(() -> CONTROLLER_CONNECTED = true)
                    .onDisconnect(() -> CONTROLLER_CONNECTED = false)
                    .onClose(code -> CONTROLLER_CONNECTED = false)
                    .onControllerError(t -> {
                        try { throw t.getThrowable(); }
                        catch (Throwable e) { throw new RuntimeException(e); }
                    })
                    .onCommunicatorError(t -> {
                        try { throw t.getThrowable(); }
                        catch (Throwable e) { throw new RuntimeException(e); }
                    })
                    .and()
                .build();
        CONTROLLER.connect();
    }
}
