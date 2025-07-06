package net.smok.macrofactory;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.function.Function;

public enum PlayerKeybind implements IConfigOptionListEntry {
    FORWARD(client -> client.options.forwardKey),
    JUMP(client -> client.options.jumpKey),
    USE(client -> client.options.useKey),
    ATTACK(client -> client.options.attackKey),
    BACKWARD(client -> client.options.backKey),
    SNEAK(client -> client.options.sneakKey)
    ;
    private final Function<MinecraftClient, KeyBinding> keyBindingGetter;

    PlayerKeybind(Function<MinecraftClient, KeyBinding> keyBindingGetter) {
        this.keyBindingGetter = keyBindingGetter;
    }


    public void setPressed(MinecraftClient client, boolean pressed) {
        keyBindingGetter.apply(client).setPressed(pressed);
    }

    public boolean isPressed(MinecraftClient client) {
        return keyBindingGetter.apply(client).isPressed();
    }

    public boolean wasPressed(MinecraftClient client) {
        return keyBindingGetter.apply(client).wasPressed();
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

            case FORWARD -> BACKWARD;
            case BACKWARD -> SNEAK;
            case SNEAK -> JUMP;
            case JUMP -> USE;
            case USE -> ATTACK;
            case ATTACK -> FORWARD;
        };
    }
    private static PlayerKeybind cycleBackward(PlayerKeybind keybind) {
        return switch (keybind) {

            case FORWARD -> ATTACK;
            case BACKWARD -> FORWARD;
            case SNEAK -> BACKWARD;
            case JUMP -> SNEAK;
            case USE -> JUMP;
            case ATTACK -> USE;
        };
    }
}
