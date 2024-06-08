package net.smok.macrofactory.guiold.selector;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.DrawContext;
import net.smok.macrofactory.guiold.PositionAlignment;
import net.smok.macrofactory.guiold.Rect;
import net.smok.macrofactory.guiold.RectContainer;
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
    private RectContainer rectContainer;


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
        int minY = height / 2 - maxLines * columnWidth / 2;
        int minX = width / 2 - maxWidth / 2;


        rectContainer = new RectContainer(containerSpace, containerSpace, minX, minX + maxWidth, minY, selectorSize);

        // Fill the rect container with widgets
        for (int line = 0, i = 0; line < maxLines; line++) {
            for (int column = 0; column < maxColumns && i < macroList.size(); column++, i++) {
                Macro macro = macroList.get(i);
                Rect rect = rectContainer.addRect(new PositionAlignment(true, selectorSize));
                MacroWidget widget = new MacroWidget(rect.x(), rect.y(), rect.width(), rect.height(), macro);
                addWidget(widget);
                macroWidgets.add(widget);
            }
            if (line < maxLines - 1) rectContainer.addLine();
        }

    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {

        String mainText = selectedWidget != null ? selectedWidget.getSelectName() : module.getName();
        drawContext.drawCenteredTextWithShadow(textRenderer, StringUtils.translate(mainText), width / 2, rectContainer.getMinY() - 20, -1);
        if (module.getAll().isEmpty())
        {
            drawContext.drawCenteredTextWithShadow(textRenderer, StringUtils.translate("guiold.empty_module"), width / 2, rectContainer.getMinY(), -1);
            return;
        }

        boolean mouseIsMove = this.lastMouseX != mouseX || this.lastMouseY != mouseY;

        if (!rectContainer.isMouseOver(mouseX, mouseY)) {
            selectedWidget = null;
            mouseIsMove = false;
        }

        for (MacroWidget widget : macroWidgets) {
            widget.render(mouseX, mouseY, widget == selectedWidget, drawContext);
            if (mouseIsMove && widget.isMouseOver(mouseX, mouseY)) selectedWidget = widget;
        }
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    @Override
    public void close() {
        super.close();
        if (selectedWidget != null) selectedWidget.callMacro(client);
    }
}
