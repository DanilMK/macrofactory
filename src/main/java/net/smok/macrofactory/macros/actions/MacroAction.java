package net.smok.macrofactory.macros.actions;

import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.smok.macrofactory.macros.Macro;
import org.jetbrains.annotations.NotNull;

public interface MacroAction {


    void start(@NotNull Minecraft minecraft, Macro macro);
    void end(@NotNull Minecraft minecraft, Macro macro);

    JsonElement getAsJsonElement();
    void setValueFromJsonElement(JsonElement element);

}
