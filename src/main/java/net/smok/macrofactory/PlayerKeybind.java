package net.smok.macrofactory;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.function.Function;

public enum PlayerKeybind implements IConfigOptionListEntry {
    FORWARD(client -> client.options.keyUp, "key.forward"),
    JUMP(client -> client.options.keyJump, "key.jump"),
    USE(client -> client.options.keyUse, "key.use"),
    ATTACK(client -> client.options.keyAttack, "key.attack"),
    BACK(client -> client.options.keyDown, "key.back"),
    SNEAK(client -> client.options.keyShift, "key.sneak")
    ;
    public static final PlayerKeybind[] VALUES = values();

    private final Function<Minecraft, KeyMapping> keyBindingGetter;
    private final String name;
    private boolean click;
    private boolean down;

    PlayerKeybind(Function<Minecraft, KeyMapping> keyBindingGetter, String name) {
        this.keyBindingGetter = keyBindingGetter;
        this.name = name;
    }

    public static void click(KeyMapping keyMapping) {
        for (PlayerKeybind value : VALUES)
            if (value.name.equals(keyMapping.getName()) && value.down) value.click = true;
    }


    public void setPressed(Minecraft client, boolean pressed) {
        if (!click || pressed) keyBindingGetter.apply(client).setDown(pressed);
        down = pressed;
        click = false;
    }

    public boolean wasClicked() {
        return click;
    }

    @Override
    public String getStringValue() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return  StringUtils.translate(name);
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward) {
        return forward ? cycleForward(this) : cycleBackward(this);
    }

    @Override
    public IConfigOptionListEntry fromString(String value) {
        return valueOf(value);
    }

    private static PlayerKeybind cycleForward(PlayerKeybind keybind) {
        return switch (keybind) {

            case FORWARD -> BACK;
            case BACK -> SNEAK;
            case SNEAK -> JUMP;
            case JUMP -> USE;
            case USE -> ATTACK;
            case ATTACK -> FORWARD;
        };
    }
    private static PlayerKeybind cycleBackward(PlayerKeybind keybind) {
        return switch (keybind) {

            case FORWARD -> ATTACK;
            case BACK -> FORWARD;
            case SNEAK -> BACK;
            case JUMP -> SNEAK;
            case USE -> JUMP;
            case ATTACK -> USE;
        };
    }
}
