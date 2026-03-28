package net.smok.macrofactory.gui.selector;

import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.smok.macrofactory.gui.MacroIcons;
import net.smok.macrofactory.macros.Macro;
import org.jetbrains.annotations.NotNull;

public class MacroWidget extends WidgetBase {

    private final Macro macro;
    private final ItemIconWidget icon;


    public MacroWidget(int x, int y, int width, int height, Macro macro) {
        super(x, y, width, height);
        this.macro = macro;
        icon = new ItemIconWidget(x, y, width, height, macro.getIcon(), MacroIcons.MACRO_EMPTY_ICON);
    }


    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, boolean selected) {
        int color = selected ? 0xE0FAFAFA : 0xE0020202;
        drawContext.drawVerticalLine(x, y, y + height, color);
        drawContext.drawVerticalLine(x + width, y, y + height, color);
        drawContext.drawHorizontalLine(x, x + width, y, color);
        drawContext.drawHorizontalLine(x, x + width, y + height, color);

        icon.render(drawContext, mouseX, mouseY, selected);
    }
    @NotNull
    public String getSelectName() {
        return macro.getSelectName();
    }

    public void callMacro(@NotNull MinecraftClient client) {
        macro.macroExecute(false, client);
    }
}
