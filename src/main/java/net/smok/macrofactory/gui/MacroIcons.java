package net.smok.macrofactory.gui;

import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.smok.macrofactory.MacroFactory;
import net.smok.macrofactory.gui.base.GuiIcon;

public enum MacroIcons implements IGuiIcon, GuiIcon {
    MACRO_EMPTY_BUTTON(0, 0),
    PLUS(0, 1),
    MINUS(0, 2),
    FOLDER_ADD(0, 3),
    FOLDER_REMOVE(0, 4),
    MACRO_ADD(0, 5),
    MACRO_REMOVE(0, 6),
    MACRO_BUTTON(0, 7),
    CONFIGURE_OFF(3, 0), CONFIGURE(6, 0),
    ENABLED_OFF(3, 1), ENABLED(6, 1),
    FOLDER_OFF(3, 2), FOLDER(6, 2),
    CHAT_OFF(3, 3), CHAT(6, 3),
    MACRO_ICON(9, 0);

    public static final Identifier TEXTURE = new Identifier(MacroFactory.MOD_ID, "textures/icons.png");
    private static final int SIZE = 20;

    private final int u;
    private final int v;

    MacroIcons(int u, int v) {
        this.u = u;
        this.v = v;
    }

    @Override
    public int getWidth() {
        return SIZE;
    }

    @Override
    public int getHeight() {
        return SIZE;
    }

    @Override
    public int getU() {
        return SIZE*u;
    }

    @Override
    public int getV() {
        return SIZE*v;
    }

    public int renderU(boolean enabled, boolean selected) {
        return getU() + (enabled ? selected ? SIZE * 2 : SIZE : 0);
    }

    @Override
    public void renderAt(int x, int y, float zLevel, boolean enabled, boolean selected) {

        int u = renderU(enabled, selected);
        int v = getV();


        RenderUtils.drawTexturedRect(x, y, u, v, getWidth(), getHeight(), zLevel);
    }

    @Override
    public void renderAt(DrawContext context, int x, int y, boolean enabled, boolean selected) {

        context.drawTexture(getTexture(), x, y, renderU(enabled, selected), getV(), getWidth(), getHeight());
    }

    @Override
    public Identifier getTexture()
    {
        return TEXTURE;
    }
}
