package net.smok.macrofactory.macros;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.smok.macrofactory.MacroFactory;
import net.smok.macrofactory.gui.selector.MacroSelectionGui;

import java.util.ArrayList;
import java.util.List;

public class Module implements IKeybindProvider {

    private final ArrayList<Macro> macros = new ArrayList<>();
    private final ConfigBoolean enabled = new ConfigBoolean("Enabled", true, null);
    private final ConfigString name = new ConfigString("Name", "module.default.new", "config.comment.module_name");
    private final ConfigHotkey guiKeybind = new ConfigHotkey("GuiKeybind", "",
            KeybindSettings.create(KeybindSettings.Context.ANY, KeyAction.BOTH, false, true, true, false),
            "config.comment.module_hotkey");
    public boolean isOpen;
    public boolean configure;


    public Module() {
        guiKeybind.getKeybind().setCallback(this::openGui);
    }


    public Module(String name) {
        this.name.setValueFromString(name);
    }
    public Module(String name, boolean open) {
        this.name.setValueFromString(name);
        this.isOpen = open;
    }



    // Public Access

    public String getName() {
        return name.getStringValue();
    }
    public ConfigString getNameConfig() {
        return name;
    }
    public void add(Macro macro) {
        macros.add(macro);
    }
    public void remove(Macro macro) {
        macros.remove(macro);
    }
    public ConfigBoolean getEnabled() {
        return enabled;
    }
    public List<Macro> getAll() {
        return macros;
    }
    public ConfigHotkey getGuiKeybind() {
        return guiKeybind;
    }

    @Override
    public void addKeysToMap(IKeybindManager manager) {
        if (enabled.getBooleanValue())
        {
            manager.addKeybindToMap(guiKeybind.getKeybind());
            for (Macro macro : macros) manager.addKeybindToMap(macro.getKeybind());
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        if (enabled.getBooleanValue())
            manager.addHotkeysForCategory(MacroFactory.MOD_ID, MacroFactory.MOD_ID+'.'+ getNameConfig().getStringValue(), getHotkeys());
    }

    private List<? extends IHotkey> getHotkeys() {
        List<IHotkey> hotkeys = new ArrayList<>();
        hotkeys.add(guiKeybind);
        for (Macro macro : macros) {
            hotkeys.add(macro.getHotkey());
        }
        return hotkeys;
    }


    // Save/load

    public JsonElement getAsJsonElement() {
        JsonArray array = new JsonArray();
        for (Macro macro : macros) array.add(macro.getAsJsonElement());

        JsonObject json = new JsonObject();
        json.add("Array", array);
        json.addProperty("Module Name", getNameConfig().getStringValue());
        json.addProperty("Enabled", enabled.getBooleanValue());
        json.add("Gui Keybind", guiKeybind.getAsJsonElement());
        return json;
    }

    public void setValueFromJsonElement(JsonElement element) {
        JsonObject json = element.getAsJsonObject();
        String value = json.get("Module Name").getAsString();
        getNameConfig().setValueFromString(value);
        enabled.setValueFromJsonElement(json.get("Enabled"));
        guiKeybind.setValueFromJsonElement(json.get("Gui Keybind"));

        JsonArray array = json.getAsJsonArray("Array");

        for (JsonElement element1 : array) {
            Macro macro = new Macro(this);
            macro.setValueFromJsonElement(element1);
            macros.add(macro);
        }
    }


    // Execute operation

    private boolean openGui(KeyAction keyAction, IKeybind keybind) {
        Screen currentScreen = MinecraftClient.getInstance().currentScreen;
        if (keyAction == KeyAction.RELEASE) {
            if (currentScreen instanceof MacroSelectionGui) {
                currentScreen.close();
                return true;
            }
        }
        else {
            if (currentScreen == null) {
                GuiBase.openGui(new MacroSelectionGui(this));
                return true;
            }
        }
        return false;
    }

}
