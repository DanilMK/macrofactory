package net.smok.macrofactory.gui;

import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.util.Identifier;
import net.smok.macrofactory.MacroFactory;

public enum MacroIcons implements IGuiIcon {
    PLUS(0, 0, 20, 20),
    MINUS(0, 20, 20, 20),
    SETTINGS(0, 40, 20, 20),
    ENABLED(0, 60, 20, 20),
    FOLDER(0, 80, 20, 20),
    FOLDER_ADD(0, 100, 20, 20),
    FOLDER_REMOVE(0, 120, 20, 20),
    MACRO_ADD(0, 140, 20, 20),
    MACRO_REMOVE(0, 160, 20, 20),
    MACRO_BUTTON(0, 180, 20, 20),
    MACRO_EMPTY_ICON(100, 0, 20, 20),
    MACRO_EMPTY_BUTTON(100, 40, 20, 20),
    CHAT(0, 200, 20, 20);

    public static final Identifier TEXTURE = new Identifier(MacroFactory.MOD_ID, "textures/icons.png");

    private final int u;
    private final int v;
    private final int w;
    private final int h;
    private final int hoverOffU;
    private final int hoverOffV;

    MacroIcons(int u, int v, int w, int h) {
        this.u = u;
        this.v = v;
        this.w = w;
        this.h = h;
        hoverOffU = w;
        hoverOffV = 0;
    }

    @Override
    public int getWidth() {
        return w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    @Override
    public int getU() {
        return u;
    }

    @Override
    public int getV() {
        return v;
    }

    @Override
    public void renderAt(int x, int y, float zLevel, boolean enabled, boolean selected) {

        int u = this.u;
        int v = this.v;

        if (enabled)
        {
            u += this.hoverOffU;
            v += this.hoverOffV;
        }

        if (selected)
        {
            u += this.hoverOffU * 2;
            v += this.hoverOffV * 2;
        }

        RenderUtils.drawTexturedRect(x, y, u, v, this.w, this.h, zLevel);
    }

    @Override
    public Identifier getTexture()
    {
        return TEXTURE;
    }
}
