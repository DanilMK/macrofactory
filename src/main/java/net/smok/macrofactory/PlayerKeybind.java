package net.smok.macrofactory;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

public enum PlayerKeybind implements IConfigOptionListEntry {
    FORWARD, JUMP, USE, ATTACK;
    private static boolean initialized;

    private KeyBinding keyBinding;

    public static void init(MinecraftClient client) {
        FORWARD.keyBinding = (client.options.forwardKey);
        USE.keyBinding = (client.options.useKey);
        ATTACK.keyBinding = (client.options.attackKey);
        JUMP.keyBinding = (client.options.jumpKey);
        initialized = true;
    }

    public void setPressed(boolean pressed) {
        if (initialized) keyBinding.setPressed(pressed);
    }

    public boolean isPressed() {
        return initialized && keyBinding.isPressed();
    }
    public boolean wasPressed() {
        return initialized && keyBinding.wasPressed();
    }

    @Override
    public String getStringValue() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return  StringUtils.translate("key."+name().toLowerCase());
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

            case FORWARD -> JUMP;
            case JUMP -> USE;
            case USE -> ATTACK;
            case ATTACK -> FORWARD;
        };
    }
    private static PlayerKeybind cycleBackward(PlayerKeybind keybind) {
        return switch (keybind) {

            case FORWARD -> ATTACK;
            case JUMP -> FORWARD;
            case USE -> JUMP;
            case ATTACK -> USE;
        };
    }
}
