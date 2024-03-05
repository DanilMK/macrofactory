package net.smok.macrofactory.gui.selector;

import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import net.minecraft.client.gui.DrawContext;
import net.smok.macrofactory.macros.ItemIcon;

public class ItemIconWidget extends WidgetBase {

    private final ItemIcon itemIcon;
    private final IGuiIcon defaultIcon;

    public ItemIconWidget(int x, int y, int width, int height, ItemIcon itemIcon, IGuiIcon defaultIcon) {
        super(x, y, width, height);
        this.itemIcon = itemIcon;
        this.defaultIcon = defaultIcon;
    }


    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {
        drawContext.drawBorder(x, y, width, height, selected ? 0xE0FAFAFA : 0xE0020202);

        if (itemIcon.isModified()) {
            itemIcon.drawIcon(drawContext, x, y, width, height);
        } else {
            bindTexture(defaultIcon.getTexture());
            defaultIcon.renderAt(x, y, 0, false, false);
        }
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return false;
    }
}
