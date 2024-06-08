package net.smok.macrofactory.guiold;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import net.minecraft.client.gui.screen.Screen;
import net.smok.macrofactory.MacroFactory;
import net.smok.macrofactory.guiold.modules.ModulesGui;
import net.smok.macrofactory.gui.ModulesScreen;

import java.util.List;

@Deprecated
public class ConfigsGui extends GuiConfigsBase {


    public ConfigsGui(Screen parent)
    {
        super(10, 50, MacroFactory.MOD_ID, parent, "guiold.title.configs");
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
        return ConfigOptionWrapper.createFor(ImmutableList.<fi.dy.masa.malilib.config.IConfigValue>of(
                ModulesScreen.SCREEN_OPEN_KEY
        ));
    }

}
