package net.smok.macrofactory.guiold.modules;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;
import net.smok.macrofactory.Configs;
import net.smok.macrofactory.guiold.*;
import net.smok.macrofactory.guiold.utils.ListEntryBox;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.Module;

public class ModuleEntry extends GuiEntry<ModuleWrapper> {

    private static final String BUTTON_MACRO_ADD = "guiold.button.macro_add";
    private static final String BUTTON_MODULE_REMOVE = "guiold.button.module_remove";
    private static final String BUTTON_MODULE_CONFIGURE = "guiold.button.module_configure";
    private static final String BUTTON_MODULE_OPEN = "guiold.button.module_open";
    private static final String BUTTON_MODULE_ENABLED = "guiold.button.module_enabled";

    public ModuleEntry(int x, int y, int width, int height, int space, GuiList<?, ?> parent, ModuleWrapper entry, int listIndex, IKeybindConfigGui host) {
        super(x, y, width, height, space, parent, entry, listIndex, host);
    }

    @Override
    public void init() {
        Module module = ModuleWrapper.getModule(entry);
        if (module == null) return;

        addGenericButton(false, MacroIcons.FOLDER_REMOVE, this::removeThisCollection, BUTTON_MODULE_REMOVE);
        addGenericButton(false, MacroIcons.MACRO_ADD, this::addNewMacro, BUTTON_MACRO_ADD);
        addSwitchButton(false, MacroIcons.CONFIGURE, module.configure, this::openConfigure, BUTTON_MODULE_CONFIGURE);
        addSwitchButton(false, MacroIcons.ENABLED, module.getEnabled().getBooleanValue(), this::enableOrDisable, BUTTON_MODULE_ENABLED);
        addSwitchButton(true, MacroIcons.FOLDER, module.isOpen.getBooleanValue(), this::openOrCloseFolder, BUTTON_MODULE_OPEN);

        //addTextField(new PositionAlignment(true), module.getNameConfig(), COMMENT_MODULE_NAME, 256);

        if (module.configure) {
            addTextField(new PositionAlignment(true), module.getNameConfig(), 30);
        } else {
            String name = module.getNameConfig().getStringValue();
            if (!name.isEmpty()) {
                addLabel(new PositionAlignment(true, Integer.MAX_VALUE, 5), GuiBase.COLOR_WHITE, name);
            }
        }


        if (module.configure) {
            addLine();

            addKeybindButton(new PositionAlignment(false, 120), module.getGuiKeybind());
        }

        addWidget(new ListEntryBox(x - 2, y + height - 1, getWidth(), 1, false, true));
    }

    private void openConfigure(ButtonBase button, int mouseButton) {
        Module module = ModuleWrapper.getModule(entry);
        if (mouseButton == 0 && entry != null) {
            module.configure = !module.configure;
            parent.refreshEntries();
        }
    }
    private void removeThisCollection(ButtonBase button, int mouseButton) {
        Module module = ModuleWrapper.getModule(entry);
        if (mouseButton == 0 && module != null) {
            Configs.Macros.Modules.remove(module);
            parent.refreshEntries();
        }
    }


    private void addNewMacro(ButtonBase button, int mouseButton) {
        Module module = ModuleWrapper.getModule(entry);
        if (mouseButton == 0 && module != null) {
            module.add(new Macro(module, "Hello world!"));
            parent.refreshEntries();
        }
    }


    private void enableOrDisable(ButtonBase button, int mouseButton) {
        Module module = ModuleWrapper.getModule(entry);
        if (mouseButton == 0 && module != null) {
            boolean enabled = !module.getEnabled().getBooleanValue();
            module.getEnabled().setBooleanValue(enabled);
            if (button instanceof ButtonSwitch buttonSwitch) buttonSwitch.setOn(enabled);
        }
    }

    private void openOrCloseFolder(ButtonBase button, int mouseButton) {
        Module module = ModuleWrapper.getModule(entry);
        if (mouseButton == 0 && module != null) {
            module.isOpen.setBooleanValue(!module.isOpen.getBooleanValue());
            if (button instanceof ButtonSwitch buttonSwitch) buttonSwitch.setOn(!module.isOpen.getBooleanValue());
            parent.refreshEntries();
        }
    }
}
