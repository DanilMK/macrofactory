package net.smok.macrofactory.gui.base;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;

public class IconWidget extends WidgetBase implements Drawable {

    private final GuiIcon icon;

    public IconWidget(int x, int y, int w, int h, GuiIcon icon) {
        super(x, y, w, h);
        this.icon = icon;
    }

    public IconWidget(int w, int h, GuiIcon icon) {
        super(w, h);
        this.icon = icon;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        icon.renderAt(context, getX(), getY(), false, false);
    }
}
