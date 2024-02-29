package net.smok.macrofactory.macros.actions;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum ActionType implements IConfigOptionListEntry {
    Player, Command;


    @Override
    public String getStringValue() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return StringUtils.translate("config.value.action_type."+name().toLowerCase());
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward) {
        return forward ? cycleForward(this) : cycleBackward(this);
    }

    @Override
    public IConfigOptionListEntry fromString(String value) {
        return valueOf(value);
    }

    private IConfigOptionListEntry cycleBackward(ActionType type) {
        return switch (type) {

            case Player -> Command;
            case Command -> Player;
        };

    }

    private IConfigOptionListEntry cycleForward(ActionType type) {
        return switch (type) {

            case Player -> Command;
            case Command -> Player;
        };
    }

}
