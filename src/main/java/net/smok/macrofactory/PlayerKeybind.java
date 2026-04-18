package net.smok.macrofactory;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.function.Function;

public enum PlayerKeybind implements IConfigOptionListEntry {
    FORWARD(client -> client.options.keyUp),
    JUMP(client -> client.options.keyJump),
    USE(client -> client.options.keyUse),
    ATTACK(client -> client.options.keyAttack),
    BACK(client -> client.options.keyDown),
    SNEAK(client -> client.options.keyShift)
    ;
    private final Function<Minecraft, KeyMapping> keyBindingGetter;

    PlayerKeybind(Function<Minecraft, KeyMapping> keyBindingGetter) {
        this.keyBindingGetter = keyBindingGetter;
    }


    public void setPressed(Minecraft client, boolean pressed) {
        keyBindingGetter.apply(client).setDown(pressed);
    }

    public boolean isPressed(Minecraft client) {
        return keyBindingGetter.apply(client).isDown();
    }

    public boolean wasPressed(Minecraft client) {
        return keyBindingGetter.apply(client).consumeClick();
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
