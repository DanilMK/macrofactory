package net.smok.macrofactory.gui.selector;

import fi.dy.masa.malilib.gui.GuiBase;
import net.smok.macrofactory.MacroFactory;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.Module;

public class MacroSelectionScreen extends GuiBase {

    private final Module module;

    public MacroSelectionScreen(Module module) {
        this.module = module;
    }

    @Override
    public void initGui() {
        super.initGui();
        int x = 0;
        int y = 0;
        int w = 40;
        int h = 40;
        int maxX = 100;
        int maxY = 100;

        for (Macro macro : module.getAll()) {
            MacroWidget widget = new MacroWidget(x, y, w, h, macro);
            addWidget(widget);

            x+= w;
            if (x >= maxX) {
                x = 0;
                y += h;
            }
            if (y >= maxY) break;
        }
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        MacroFactory.LOGGER.info("Mouse move "+mouseX+":"+mouseY);
    }
}
