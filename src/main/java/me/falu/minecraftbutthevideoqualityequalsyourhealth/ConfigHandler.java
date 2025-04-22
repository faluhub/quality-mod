package me.falu.minecraftbutthevideoqualityequalsyourhealth;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigHandler {
    private static File FILE = FabricLoader.getInstance().getConfigDir().resolve("value_config.json").toFile();

    public static void init() {
        if (!FILE.exists()) {
            try {
                boolean ignored = FILE.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        JsonObject config = get(false);
        if (config.has("file")) {
            FILE = new File(config.get("file").getAsString());
            init();
        }
    }

    private static JsonObject get() {
        return get(true);
    }

    private static JsonObject get(boolean initialise) {
        if (initialise) {
            init();
        }
        try {
            FileReader reader = new FileReader(FILE);
            Object obj = JsonParser.parseReader(reader);
            reader.close();
            return JsonNull.INSTANCE.equals(obj) ? new JsonObject() : (JsonObject) obj;
        } catch (IOException ignored) {
        }

        return new JsonObject();
    }

    private static void write(JsonObject config) {
        try {
            FileWriter writer = new FileWriter(FILE);

            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(config));
            writer.flush();
            writer.close();
        } catch (IOException ignored) {
        }
    }

    private static JsonElement getValue(String key) {
        JsonObject config = get();
        return config.get(key);
    }

    public static int getIntValue(String key, int def) {
        JsonElement element = getValue(key);
        if (element == null || element.isJsonNull()) {
            JsonObject config = get();
            config.addProperty(key, def);
            write(config);
            return def;
        }
        return element.getAsInt();
    }

    public static float getFloatValue(String key, float def) {
        JsonElement element = getValue(key);
        if (element == null || element.isJsonNull()) {
            JsonObject config = get();
            config.addProperty(key, def);
            write(config);
            return def;
        }
        return element.getAsFloat();
    }

    public static String getStringValue(String key, String def) {
        JsonElement element = getValue(key);
        if (element == null || element.isJsonNull()) {
            JsonObject config = get();
            config.addProperty(key, def);
            write(config);
            return def;
        }
        return element.getAsString();
    }

    public static boolean getBooleanValue(String key, boolean def) {
        JsonElement element = getValue(key);
        if (element == null || element.isJsonNull()) {
            JsonObject config = get();
            config.addProperty(key, def);
            write(config);
            return def;
        }
        return element.getAsBoolean();
    }
}
