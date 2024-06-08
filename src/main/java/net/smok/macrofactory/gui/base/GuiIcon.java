package net.smok.macrofactory.gui.base;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public interface GuiIcon {

    void renderAt(DrawContext context, int x, int y, boolean enabled, boolean selected);

    Identifier getTexture();

    int getWidth();

    int getHeight();

    int getU();

    int getV();
}
