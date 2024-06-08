package net.smok.macrofactory.gui;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import net.minecraft.text.Text;
import net.smok.macrofactory.Configs;
import net.smok.macrofactory.HotKeyWithCallBack;
import net.smok.macrofactory.guiold.MacroIcons;
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

        ModulesList modulesList = addDrawableChild(new ModulesList(20, 50, width - 50, height - 100, this));
        modulesList.setSpace(3);


        //addDrawable(new BoxWidget(10, 100, 100, 40));

        GuiIcon folderIcon = MacroIcons.FOLDER_ADD;
        int folderX = width - folderIcon.getWidth();
        int folderY = 25;
        ButtonBase folderButton = new ButtonBase(folderX, folderY, folderIcon, button -> {
            Configs.Macros.Modules.add(new Module("Mew Module", true));
            modulesList.refreshPositions();
        /*
        widgetEntries.clear();
        nextPosition = getEntryStartY();
        MacroFactory.LOGGER.info("Refresh entries");

        if (widgetSearchBar == null || !widgetSearchBar.hasFilter()) addNonFilteredContents(this::createWidgetEntry);
        else addFilteredContents(this::createWidgetEntry);*/
        });

        addDrawableChild(folderButton);

    }


}
