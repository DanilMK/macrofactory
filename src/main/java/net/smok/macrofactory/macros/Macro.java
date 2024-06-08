package net.smok.macrofactory.macros;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.smok.macrofactory.HotKeyWithCallBack;
import net.smok.macrofactory.PlayerKeybind;
import net.smok.macrofactory.TickLoop;
import net.smok.macrofactory.macros.actions.*;
import org.jetbrains.annotations.NotNull;


public class Macro {


    // Configs
    private final ConfigString name = new ConfigString("Name", "", "config.comment.macro_name");
    private final ConfigHotkey hotkey = new HotKeyWithCallBack("Hotkey", "", KeybindSettings.INGAME_BOTH,"config.comment.macro_hotkey", this::onKeyAction);
    private final ConfigOptionList actionType = new ConfigOptionList("Type", ActionType.Command, "config.comment.type");
    private final ConfigOptionList callType = new ConfigOptionList("Call", CallType.SINGLE, "config.comment.call");
    private final ConfigInteger delay = new ConfigInteger("Delay", 0, 0, 20*60, "config.comment.delay");
    private final ItemIcon icon = new ItemIcon("Icon", ItemStack.EMPTY, "config.comment.icon");

    private final PlayerAction playerAction = new PlayerAction("Action", "config.comment.player_action");
    private final CommandAction commandAction = new CommandAction("Action", "guiold.button.chat" , "config.comment.command_action");

    public boolean configure;
    private final ConfigBoolean isConfigure = new ConfigBoolean("Configure", false, "guiold.button.macro_configure");

    private final Module module;


    private boolean enable;
    private int cd;


    public Macro(Module module) {
        this.module = module;
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
        this.commandAction.getCommand().setValueFromString(firstCommand);
        actionType.setOptionListValue(ActionType.Command);
    }
    public Macro(Module module, String firstCommand) {
        this(module);
        this.commandAction.getCommand().setValueFromString(firstCommand);
        actionType.setOptionListValue(ActionType.Command);
    }
    public Macro(Module module, String name, String defaultHotkeys, boolean looped, int delay, PlayerKeybind playerActionKeybind) {
        this(module);
        this.name.setValueFromString(name);
        this.hotkey.setValueFromString(defaultHotkeys);
        this.delay.setIntegerValue(delay);
        this.callType.setOptionListValue(looped ? CallType.REPEAT : CallType.SINGLE);
        this.playerAction.setOptionListValue(playerActionKeybind);
        actionType.setOptionListValue(ActionType.Player);
    }

    // Access


    public ConfigBoolean getIsConfigure() {
        return isConfigure;
    }
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
    public ConfigInteger getDelayConfig() {
        return delay;
    }
    public ConfigOptionList getCallType() {
        return callType;
    }
    public ItemIcon getIcon() {
        return icon;
    }

    @NotNull
    public String getSelectName() {
        if (!name.getStringValue().isEmpty()) return name.getStringValue();

        switch ((ActionType)actionType.getOptionListValue()) {

            case Player -> {
                return playerAction.getOptionListValue().getDisplayName();
            }
            case Command -> {
                return commandAction.getCommand().getStringValue();
            }
        }
        return "";
    }


    // Save/load

    public JsonElement getAsJsonElement() {
        JsonObject json = new JsonObject();
        SmokUtils.getAsJsonElement(json, name);
        SmokUtils.getAsJsonElement(json, hotkey);
        SmokUtils.getAsJsonElement(json, delay);
        SmokUtils.getAsJsonElement(json, actionType);
        SmokUtils.getAsJsonElement(json, callType);
        SmokUtils.getAsJsonElement(json, icon);

        switch ((ActionType)actionType.getOptionListValue()) {

            case Player -> SmokUtils.getAsJsonElement(json, playerAction);
            case Command -> json.add(commandAction.getName(), commandAction.getAsJsonElement());
        }

        return json;
    }

    public void setValueFromJsonElement(JsonElement element) {
        JsonObject json = element.getAsJsonObject();
        SmokUtils.setValueFromJsonElement(json, name);
        SmokUtils.setValueFromJsonElement(json, hotkey);
        SmokUtils.setValueFromJsonElement(json, delay);
        SmokUtils.setValueFromJsonElement(json, actionType);
        SmokUtils.setValueFromJsonElement(json, callType);
        SmokUtils.setValueFromJsonElement(json, icon);



        switch ((ActionType)actionType.getOptionListValue()) {

            case Player -> SmokUtils.setValueFromJsonElement(json, playerAction);
            case Command -> commandAction.setValueFromJsonElement(json.get(commandAction.getName()));
        }

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
            cd += delay.getIntegerValue();
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
