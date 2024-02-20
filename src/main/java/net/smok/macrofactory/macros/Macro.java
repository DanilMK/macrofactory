package net.smok.macrofactory.macros;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import net.minecraft.client.MinecraftClient;
import net.smok.macrofactory.PlayerKeybind;
import net.smok.macrofactory.TickLoop;
import net.smok.macrofactory.macros.actions.*;
import org.jetbrains.annotations.NotNull;


public class Macro {


    // Configs
    private final ConfigString name = new ConfigString("Name", "", "config.comment.macro_name");
    private final ConfigHotkey hotkey = new ConfigHotkey("Hotkey", "", null);
    private final ConfigOptionList actionType = new ConfigOptionList("Type", ActionType.Command, "config.comment.type");
    private final ConfigOptionList callType = new ConfigOptionList("Call", CallType.SINGLE, "config.comment.call");
    private final ConfigInteger coolDown = new ConfigInteger("CoolDown", 0, 0, 20*60, "config.comment.cooldown");

    private final PlayerAction playerAction = new PlayerAction("Action", "config.comment.player_action");
    private final CommandAction commandAction = new CommandAction("Action", "config.comment.command_action");
    private final ConfigBoolean inChat = new ConfigBoolean("Chat", false, null);

    public boolean configure;

    private final Module module;


    private boolean enable;
    private int cd;


    public Macro(Module module) {
        this.module = module;
        hotkey.getKeybind().setSettings(KeybindSettings.INGAME_BOTH);
        hotkey.getKeybind().setCallback(this::onKeyAction);
    }
    public Macro(Module module, String name, String defaultHotkeys) {
        this(module);
        this.name.setValueFromString(name);
        this.hotkey.setValueFromString(defaultHotkeys);
    }
    public Macro(Module module, String name, String defaultHotkeys, String firstCommand) {
        this(module);
        this.name.setValueFromString(name);
        this.hotkey.setValueFromString(defaultHotkeys);
        this.commandAction.setValueFromString(firstCommand);
        actionType.setOptionListValue(ActionType.Command);
    }
    public Macro(Module module, String firstCommand) {
        this(module);
        this.commandAction.setValueFromString(firstCommand);
        actionType.setOptionListValue(ActionType.Command);
    }
    public Macro(Module module, String name, String defaultHotkeys, boolean looped, int coolDown, PlayerKeybind playerActionKeybind) {
        this(module);
        this.name.setValueFromString(name);
        this.hotkey.setValueFromString(defaultHotkeys);
        this.coolDown.setIntegerValue(coolDown);
        this.callType.setOptionListValue(looped ? CallType.REPEAT : CallType.SINGLE);
        this.playerAction.setOptionListValue(playerActionKeybind);
        actionType.setOptionListValue(ActionType.Player);
    }

    // Access

    public ConfigHotkey getHotkey() {
        return hotkey;
    }
    public IKeybind getKeybind() {
        return hotkey.getKeybind();
    }
    public String getName() {
        return name.getStringValue();
    }
    public Module getModule() {
        return module;
    }
    public PlayerAction getPlayerAction() {
        return playerAction;
    }
    public CommandAction getCommandAction() {
        return commandAction;
    }
    public ConfigString getNameConfig() {
        return name;
    }
    public ConfigOptionList getActionType() {
        return actionType;
    }
    public IConfigValue getCoolDownConfig() {
        return coolDown;
    }
    public ConfigOptionList getCallType() {
        return callType;
    }
    public void setInChat(boolean value) {
        inChat.setBooleanValue(
                actionType.getOptionListValue() == ActionType.Command &&
                        callType.getOptionListValue() == CallType.SINGLE &&
                        value);
    }
    public boolean isChat() {
        return inChat.getBooleanValue();
    }


    // Save/load

    public JsonElement getAsJsonElement() {
        JsonObject json = new JsonObject();
        getAsJsonElement(json, name);
        getAsJsonElement(json, hotkey);
        getAsJsonElement(json, coolDown);
        getAsJsonElement(json, actionType);
        getAsJsonElement(json, callType);

        switch ((ActionType)actionType.getOptionListValue()) {

            case Player -> getAsJsonElement(json, playerAction);
            case Command -> getAsJsonElement(json, commandAction);
        }
        if (actionType.getOptionListValue() == ActionType.Command && callType.getOptionListValue() == CallType.SINGLE)
            getAsJsonElement(json, inChat);

        return json;
    }

    public void setValueFromJsonElement(JsonElement element) {
        JsonObject json = element.getAsJsonObject();
        setValueFromJsonElement(json, name);
        setValueFromJsonElement(json, hotkey);
        setValueFromJsonElement(json, coolDown);
        setValueFromJsonElement(json, actionType);

        setValueFromJsonElement(json, callType);


        switch ((ActionType)actionType.getOptionListValue()) {

            case Player -> setValueFromJsonElement(json, playerAction);
            case Command -> setValueFromJsonElement(json, commandAction);
        }

        if (actionType.getOptionListValue() == ActionType.Command && callType.getOptionListValue() == CallType.SINGLE)
            setValueFromJsonElement(json, inChat);

    }


    private static void setValueFromJsonElement(JsonObject json, IConfigBase configBase) {
        configBase.setValueFromJsonElement(json.get(configBase.getName()));
    }
    private static void getAsJsonElement(JsonObject json, IConfigBase configBase) {
        json.add(configBase.getName(), configBase.getAsJsonElement());
    }





    // Execute macro

    private boolean onKeyAction(KeyAction keyAction, IKeybind key) {
        MinecraftClient client = MinecraftClient.getInstance();
        macroExecute(keyAction == KeyAction.PRESS, client);
        return true;
    }

    public void macroExecute(boolean press, @NotNull MinecraftClient client) {
        switch ((CallType)callType.getOptionListValue()) {

            case SINGLE -> {
                if (!press)
                    getAction().run(client, MacroAction.Loop.TICK, this);
            }
            case REPEAT -> {
                if (!press) {
                    if (enable) TickLoop.removeFromLoop(this);
                    else {
                        startLoop(client);
                        TickLoop.addToLoop(this);
                    }
                }
            }
            case HOLD -> {
                if (press) {
                    startLoop(client);
                    TickLoop.addToLoop(this);
                } else TickLoop.removeFromLoop(this);
            }
        }
    }

    public void tickLoop(MinecraftClient client) {
        if (cd <= 0) {
            cd += coolDown.getIntegerValue();
            getAction().run(client, MacroAction.Loop.TICK, this);

        } else {
            getAction().run(client, MacroAction.Loop.OFF_TICK, this);
        }
        if (cd > 0) cd--;
    }

    public void startLoop(MinecraftClient client) {
        enable = true;
        getAction().run(client, MacroAction.Loop.START, this);
    }

    public void endLoop(MinecraftClient client) {
        enable = false;
        getAction().run(client, MacroAction.Loop.END, this);
    }

    private MacroAction getAction() {
        return switch ((ActionType)actionType.getOptionListValue()) {

            case Player -> playerAction;
            case Command -> commandAction;
        };
    }


}
