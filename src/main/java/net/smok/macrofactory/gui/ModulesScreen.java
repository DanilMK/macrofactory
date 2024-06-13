package net.smok.macrofactory.gui;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.smok.macrofactory.Configs;
import net.smok.macrofactory.HotKeyWithCallBack;
import net.smok.macrofactory.ModulesKeybindProvider;
import net.smok.macrofactory.gui.base.*;
import net.smok.macrofactory.gui.module.ModulesList;
import net.smok.macrofactory.macros.Module;


public class ModulesScreen extends GuiBase {

    public static final ConfigHotkey SCREEN_OPEN_KEY = new HotKeyWithCallBack("cmdMacroOpen", "Y", "Menu Open", (action, key) -> {
        fi.dy.masa.malilib.gui.GuiBase.openGui(new  ModulesScreen());
        return true;
    });

    public ModulesScreen() {
        super(Text.translatable("gui.title.screen_module"));
    }

    public ModulesScreen(Screen parent) {
        super(Text.of("Modules"));
    }


    /*

        *BOOLEAN,
        *INTEGER,
        *DOUBLE,
        COLOR, todo
        *STRING,
        STRING_LIST, todo
        COLOR_LIST, todo
        *OPTION_LIST,
        *HOTKEY;
     */


    @Override
    protected void init() {
        super.init();

        ModulesList modulesList = addDrawableChild(new ModulesList(20, 50, width - 50, height - 100, 3, this));


        GuiIcon folderIcon = MacroIcons.FOLDER_ADD;
        int folderX = width - folderIcon.getWidth();
        int folderY = 25;
        ButtonBase folderButton = new ButtonBase(folderX, folderY, folderIcon, button -> {
            Configs.Macros.Modules.add(new Module("Mew Module", true));
            modulesList.refreshPositions();
        });

        addDrawableChild(folderButton);

    }

    @Override
    public void close() {
        super.close();
        ModulesKeybindProvider.update();
    }
}
