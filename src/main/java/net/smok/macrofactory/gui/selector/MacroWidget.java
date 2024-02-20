package net.smok.macrofactory.gui.selector;

import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.smok.macrofactory.macros.Macro;
import org.jetbrains.annotations.NotNull;

public class MacroWidget extends WidgetBase {

    private final Macro macro;


    public MacroWidget(int x, int y, int width, int height, Macro macro) {
        super(x, y, width, height);
        this.macro = macro;
    }

    public void apply(@NotNull MinecraftClient client) {
        macro.macroExecute(false, client);
    }

    public void select() {

    }

    public void deselect() {

    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {

        ItemStack stone = Items.STONE.getDefaultStack();
        drawContext.drawItem(stone, x, y);
        if (selected)
        {
            RenderUtils.drawOutlinedBox(this.x, this.y, this.width, this.height, 0x20C0C0C0, 0xE0FFFFFF);
        }
    }
}
