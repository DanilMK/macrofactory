package net.smok.macrofactory.guiold.utils;

import fi.dy.masa.malilib.gui.widgets.WidgetHoverInfo;

public class SwitchComment extends WidgetHoverInfo {

    private final String key1, key2;

    public SwitchComment(int x, int y, int width, int height, boolean enable, String key1, String key2, Object... args) {
        super(x, y, width, height, key1, args);
        this.key1 = key1;
        this.key2 = key2;
        setEnable(enable);
    }

    public void setEnable(boolean enable) {
        lines.clear();
        setInfoLines(enable ? key1 : key2);
    }
}
