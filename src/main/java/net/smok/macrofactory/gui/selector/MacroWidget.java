package net.smok.macrofactory.gui.selector;

import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.smok.macrofactory.gui.MacroIcons;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.actions.ActionType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MacroWidget extends WidgetBase {

    private final Macro macro;


    public MacroWidget(int x, int y, int width, int height, Macro macro) {
        super(x, y, width, height);
        this.macro = macro;
    }


    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {

        //ItemStack stone = Items.STONE.getDefaultStack();
        //drawContext.drawItem(stone, x + width / 2 - 10, y + height / 2 - 10);

        drawContext.drawBorder(x, y, width, height, selected ? 0xE0FAFAFA : 0xE0020202);

        bindTexture(MacroIcons.MACRO_EMPTY_ICON.getTexture());
        MacroIcons.MACRO_EMPTY_ICON.renderAt(x, y, 0, false, false);

    }
    @NotNull
    public String getSelectName() {
        return macro.getSelectName();
    }

    public void callMacro(@NotNull MinecraftClient client) {
        macro.macroExecute(false, client);
    }
}
