package net.smok.macrofactory.macros;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.util.StringUtils;

public enum CallType implements IConfigOptionListEntry {
    SINGLE, REPEAT, HOLD;

    @Override
    public String getStringValue() {
        return name();
    }

    @Override
    public String getDisplayName() {
        return StringUtils.translate("config.value.call_type."+name().toLowerCase());
    }

    @Override
    public IConfigOptionListEntry cycle(boolean forward) {
        return forward ? cycleForward(this) : cycleBackward(this);
    }

    @Override
    public IConfigOptionListEntry fromString(String value) {
        return valueOf(value);
    }


    private IConfigOptionListEntry cycleForward(CallType type) {
        return switch (type) {
            case SINGLE -> REPEAT;
            case REPEAT -> HOLD;
            case HOLD -> SINGLE;
        };
    }
    private IConfigOptionListEntry cycleBackward(CallType type) {
        return switch (type) {
            case SINGLE -> HOLD;
            case REPEAT -> SINGLE;
            case HOLD -> REPEAT;
        };
    }
}
