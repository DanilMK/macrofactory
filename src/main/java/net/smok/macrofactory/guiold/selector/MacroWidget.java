package net.smok.macrofactory.guiold.selector;

import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.smok.macrofactory.guiold.MacroIcons;
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
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext) {
        drawContext.drawBorder(x, y, width, height, selected ? 0xE0FAFAFA : 0xE0020202);

        icon.render(mouseX, mouseY, selected, drawContext);
    }
    @NotNull
    public String getSelectName() {
        return macro.getSelectName();
    }

    public void callMacro(@NotNull MinecraftClient client) {
        macro.macroExecute(false, client);
    }
}
