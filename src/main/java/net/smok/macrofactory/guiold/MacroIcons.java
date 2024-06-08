package net.smok.macrofactory.guiold;

import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.smok.macrofactory.MacroFactory;
import net.smok.macrofactory.gui.base.GuiIcon;

public enum MacroIcons implements IGuiIcon, GuiIcon {
    PLUS(0, 0, 20, 20),
    MINUS(0, 20, 20, 20),
    CONFIGURE(0, 40, 20, 20),
    CONFIGURE_OFF(0, 40, 20, 20), // todo
    ENABLED(0, 60, 20, 20),
    ENABLED_OFF(0, 60, 20, 20), // todo
    FOLDER(0, 80, 20, 20),
    FOLDER_OFF(0, 80, 20, 20), // todo
    FOLDER_ADD(0, 100, 20, 20),
    FOLDER_REMOVE(0, 120, 20, 20),
    MACRO_ADD(0, 140, 20, 20),
    MACRO_REMOVE(0, 160, 20, 20),
    MACRO_BUTTON(0, 180, 20, 20),
    MACRO_EMPTY_ICON(100, 0, 20, 20),
    MACRO_EMPTY_BUTTON(100, 40, 20, 20),
    CHAT(0, 200, 20, 20),
    CHAT_OFF(0, 200, 20, 20); // todo

    public static final Identifier TEXTURE = new Identifier(MacroFactory.MOD_ID, "textures/icons.png");

    private final int u;
    private final int v;
    private final int w;
    private final int h;
    private final int hoverOffU;

    MacroIcons(int u, int v, int w, int h) {
        this.u = u;
        this.v = v;
        this.w = w;
        this.h = h;
        hoverOffU = w;
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

    public int renderU(boolean enabled, boolean selected) {
        return this.u + (enabled ? selected ? hoverOffU * 2 : hoverOffU : 0);
    }

    @Override
    public void renderAt(int x, int y, float zLevel, boolean enabled, boolean selected) {

        int u = renderU(enabled, selected);
        int v = this.v;


        RenderUtils.drawTexturedRect(x, y, u, v, this.w, this.h, zLevel);
    }

    @Override
    public void renderAt(DrawContext context, int x, int y, boolean enabled, boolean selected) {

        context.drawTexture(getTexture(), x, y, renderU(enabled, selected), v, w, h);
    }

    @Override
    public Identifier getTexture()
    {
        return TEXTURE;
    }
}
