package net.smok.macrofactory.macros;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
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
    private final CommandAction commandAction = new CommandAction("Action", "gui.button.chat" , "config.comment.command_action");

    public boolean configure;
    public boolean markAsDeleted;

    private final Module module;


    private boolean enable;
    private int cd;

    public int getCd() {
        return cd;
    }

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
    public IConfigValue getDelayConfig() {
        return delay;
    }
    public ConfigOptionList getCallType() {
        return callType;
    }
    public ItemIcon getIcon() {
        return icon;
    }

    public boolean isModified() {
        return playerAction.isModified() || commandAction.getCommand().isModified();
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
        Minecraft client = Minecraft.getInstance();
        switch ((CallType)callType.getOptionListValue()) {

            case SINGLE -> {
                if (keyAction == KeyAction.PRESS) {
                    startLoop(client, 10000);
                    TickLoop.addToLoop(this);
                } else TickLoop.removeFromLoop(this);
                //getAction().run(client, press ? MacroAction.Loop.TICK : MacroAction.Loop.END, this);
            }
            case REPEAT -> {
                if (keyAction == KeyAction.RELEASE) {
                    if (enable) TickLoop.removeFromLoop(this);
                    else {
                        startLoop(client, 0);
                        TickLoop.addToLoop(this);
                    }
                }
            }
            case HOLD -> {
                if (keyAction == KeyAction.PRESS) {
                    startLoop(client, 10);
                    TickLoop.addToLoop(this);
                } else if (keyAction == KeyAction.RELEASE)
                    TickLoop.removeFromLoop(this);
            }
        }
        return true;
    }


    public void macroExecute(@NotNull Minecraft client) {
        switch ((CallType)callType.getOptionListValue()) {

            case SINGLE, HOLD -> {
                TickLoop.addToLoop(this);
                startLoop(client, 0);
                TickLoop.removeFromLoop(this);
            }
            case REPEAT -> {
                if (enable) TickLoop.removeFromLoop(this);
                else {
                    startLoop(client, 0);
                    TickLoop.addToLoop(this);
                }
            }
        }
    }

    public void tickLoop(Minecraft client) {
        if (--cd <= 0) {
            cd += delay.getIntegerValue();
            getAction().start(client, this);

        } else {
            getAction().end(client, this);
        }
    }

    public void startLoop(Minecraft client, int delay) {
        enable = true;
        getAction().start(client, this);
        cd = delay;
    }

    public void endLoop(Minecraft client) {
        enable = false;
        getAction().end(client, this);
    }

    private MacroAction getAction() {
        return switch ((ActionType)actionType.getOptionListValue()) {

            case Player -> playerAction;
            case Command -> commandAction;
        };
    }


}
