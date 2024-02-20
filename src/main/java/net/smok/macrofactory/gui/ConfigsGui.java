package net.smok.macrofactory.gui;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import net.minecraft.client.gui.screen.Screen;
import net.smok.macrofactory.Configs;
import net.smok.macrofactory.MacroFactory;
import net.smok.macrofactory.gui.modules.ModulesGui;

import java.util.List;

public class ConfigsGui extends GuiConfigsBase {


    public ConfigsGui(Screen parent)
    {
        super(10, 50, MacroFactory.MOD_ID, parent, "gui.title.configs");
        setParent(parent);
    }


    @Override
    public void initGui()
    {
        super.initGui();

        this.clearOptions();

        int x = 10;
        int y = 26;

        ButtonGeneric button = new ButtonGeneric(x, y, -1, 20, "macro");
        this.addButton(button, (button1, mouseButton) -> GuiBase.openGui(new ModulesGui(this)));
    }


    @Override
    public List<ConfigOptionWrapper> getConfigs()
    {
        return ConfigOptionWrapper.createFor(Configs.Generic.OPTIONS);
    }

}
