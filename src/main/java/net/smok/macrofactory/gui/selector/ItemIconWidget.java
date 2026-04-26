package net.smok.macrofactory.gui.selector;

import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.render.GuiContext;
import net.minecraft.world.item.ItemStack;
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
    public void render(GuiContext drawContext, int mouseX, int mouseY, boolean selected) {

        ItemStack itemStack = itemIcon.getItemStack();
        if (itemStack.isEmpty()) {
            defaultIcon.renderAt(drawContext, x, y, 0, false, false);
        } else {
            drawContext.item(itemIcon.getItemStack(), x + width / 2 - 8, y + height / 2 - 8);
        }
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return false;
    }
}
