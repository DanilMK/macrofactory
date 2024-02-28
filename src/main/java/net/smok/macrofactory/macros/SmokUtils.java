package net.smok.macrofactory.macros;

import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.IConfigBase;
import org.jetbrains.annotations.NotNull;

public final class SmokUtils {

    public static void setValueFromJsonElement(@NotNull JsonObject json, @NotNull IConfigBase configBase) {
        configBase.setValueFromJsonElement(json.get(configBase.getName()));
    }

    public static void getAsJsonElement(@NotNull JsonObject json, @NotNull IConfigBase configBase) {
        json.add(configBase.getName(), configBase.getAsJsonElement());
    }
}
