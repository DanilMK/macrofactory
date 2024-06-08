package net.smok.macrofactory.guiold.utils;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;

public class ListEntryBox extends WidgetBase implements Drawable {

    private final boolean hasVertical, hasHorizontal;
    public ListEntryBox(int x, int y, int width, int height, boolean hasVertical, boolean hasHorizontal) {
        super(x, y, width, height);
        this.hasVertical = hasVertical;
        this.hasHorizontal = hasHorizontal;
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {
        if (hasVertical) drawContext.drawVerticalLine(x, y, y + height, GuiBase.COLOR_HORIZONTAL_BAR);
        if (hasHorizontal) drawContext.drawHorizontalLine(x, x + width, y, GuiBase.COLOR_HORIZONTAL_BAR);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        render(mouseX, mouseY, false, context);
    }
}
