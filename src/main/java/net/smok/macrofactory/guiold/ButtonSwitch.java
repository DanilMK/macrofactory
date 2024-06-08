package net.smok.macrofactory.guiold;

import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;

public class ButtonSwitch extends ButtonGeneric {

    private boolean on;

    public ButtonSwitch(int x, int y, IGuiIcon icon, boolean on) {
        super(x, y, icon);
        this.on = on;
    }


    public void setOn(boolean on) {
        this.on = on;
    }

    @Override
    protected int getTextureOffset(boolean isMouseOver)
    {
        if (!this.enabled) return 0;
        if (on) return isMouseOver ? 4 : 3;
        return isMouseOver ? 2 : 1;
    }
}
