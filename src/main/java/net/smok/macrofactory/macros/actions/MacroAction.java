package net.smok.macrofactory.macros.actions;

import com.google.gson.JsonElement;
import net.minecraft.client.MinecraftClient;
import net.smok.macrofactory.macros.Macro;
import org.jetbrains.annotations.NotNull;

public interface MacroAction {

    enum Loop {
        START, END, TICK, OFF_TICK
    }

    void run(@NotNull MinecraftClient client, Loop loop, Macro macro);

    JsonElement getAsJsonElement();
    void setValueFromJsonElement(JsonElement element);

}
