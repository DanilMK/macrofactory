package net.smok.macrofactory.gui.selector;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.smok.macrofactory.gui.PositionAlignment;
import net.smok.macrofactory.gui.Rect;
import net.smok.macrofactory.gui.RectContainer;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.Module;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MacroSelectionGui extends GuiBase {

    private final static int selectorSize = 20;
    private final static int containerSpace = 5;
    private final static int columnWidth = selectorSize + containerSpace;


    private final Module module;
    private final List<MacroWidget> macroWidgets = new ArrayList<>();



    @Nullable
    private MacroWidget selectedWidget;
    private int lastMouseX;
    private int lastMouseY;
    private int minY;


    public MacroSelectionGui(Module module) {
        this.module = module;
    }

    @Override
    public void initGui() {
        super.initGui();
        List<Macro> macroList = module.getAll();



        // Create bounds for widgets
        int maxColumns = Math.min(5, width / columnWidth);
        int maxLines = Math.min(5, height / columnWidth);
        if (macroList.size() < 5) maxColumns = macroList.size();
        if (macroList.size() / 5 < 5) maxLines = macroList.size() / 5 + 1;

        int maxWidth = maxColumns * columnWidth - containerSpace;
        minY = height / 2 - maxLines * columnWidth / 2;
        int minX = width / 2 - maxWidth / 2;


        // Fill the rect container with widgets
        for (int line = 0, i = 0; line < maxLines; line++) {
            for (int column = 0; column < maxColumns && i < macroList.size(); column++, i++) {
                Macro macro = macroList.get(i);
                MacroWidget widget = new MacroWidget(minX + column * columnWidth, minY + line * columnWidth, selectorSize, selectorSize, macro);
                addWidget(widget);
                macroWidgets.add(widget);
            }
        }

    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor drawContext, int mouseX, int mouseY, float partialTicks) {

        String mainText = selectedWidget != null ? selectedWidget.getSelectName() : module.getName();
        drawContext.centeredText(font, StringUtils.translate(mainText), width / 2, minY - 20, -1);
        if (module.getAll().isEmpty())
        {
            drawContext.centeredText(font, StringUtils.translate("gui.empty_module"), width / 2, minY, -1);
            return;
        }


        boolean mouseIsMove = this.lastMouseX != mouseX || this.lastMouseY != mouseY;
        if (mouseIsMove) {
            selectedWidget = null;

            for (MacroWidget widget : macroWidgets) if (widget.isMouseOver(mouseX, mouseY)) selectedWidget = widget;
        }

        for (MacroWidget widget : macroWidgets)
            widget.render(GuiContext.fromGuiGraphics(drawContext), mouseX, mouseY, widget == selectedWidget);

        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    @Override
    public void onClose() {
        super.onClose();
        if (selectedWidget != null) selectedWidget.callMacro(minecraft);
    }
}
