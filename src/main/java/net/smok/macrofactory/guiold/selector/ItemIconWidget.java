package net.smok.macrofactory.guiold.selector;

import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.smok.macrofactory.gui.base.GuiIcon;
import net.smok.macrofactory.gui.base.IconWidget;
import net.smok.macrofactory.macros.ItemIcon;

public class ItemIconWidget extends WidgetBase implements Drawable {

    private final ItemIcon itemIcon;
    private final IconWidget defaultIcon;

    public ItemIconWidget(int x, int y, int width, int height, ItemIcon itemIcon, GuiIcon defaultIcon) {
        super(x, y, width, height);
        this.itemIcon = itemIcon;
        this.defaultIcon = new IconWidget(x, y, width, height, defaultIcon);
    }


    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {
        drawContext.drawBorder(x, y, width, height, selected ? 0xE0FAFAFA : 0xE0020202);

        if (itemIcon.isModified()) {
            itemIcon.drawIcon(drawContext, x, y, width, height);
        } else {
            defaultIcon.render(drawContext, mouseX, mouseY, 0);
        }
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        render(mouseX, mouseY, super.isMouseOver(mouseX, mouseY), context);
    }
}
