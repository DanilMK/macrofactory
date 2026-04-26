package net.smok.macrofactory.gui.modules;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import net.smok.macrofactory.Configs;
import net.smok.macrofactory.gui.*;
import net.smok.macrofactory.gui.utils.ListEntryBox;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.Module;

public class ModuleEntry extends GuiEntry<ModuleWrapper> {

    private static final String BUTTON_MACRO_ADD = "gui.button.macro_add";
    private static final String BUTTON_MODULE_REMOVE = "gui.button.module_remove";
    private static final String BUTTON_MODULE_CONFIGURE = "gui.button.module_configure";
    private static final String BUTTON_MODULE_OPEN = "gui.button.module_open";
    private static final String BUTTON_MODULE_ENABLED = "gui.button.module_enabled";

    public ModuleEntry(int x, int y, int width, int height, int space, GuiList<?, ?> parent, ModuleWrapper entry, int listIndex, IKeybindConfigGui host) {
        super(x, y, width, height, space, parent, entry, listIndex, host);
    }

    @Override
    public void init() {
        Module module = ModuleWrapper.getModule(entry);
        if (module == null) return;
        int x = getX();
        int rightX = x + getWidth();
        int lineHeight = getHeight();
        int y = getY();

        ButtonGeneric removeBtn = addButton(new ButtonGeneric(rightX -= bSize, y, MacroIcons.FOLDER_REMOVE), (_, mouseButton1) -> removeThisCollection(mouseButton1, module));
        addCommentForWidget(removeBtn, BUTTON_MODULE_REMOVE);

        ButtonGeneric addBtn = addButton(new ButtonGeneric(rightX -= bSize, y, MacroIcons.MACRO_ADD), (_, mouseButton1) -> addNewMacro(mouseButton1, module));
        addCommentForWidget(addBtn, BUTTON_MACRO_ADD);

        ButtonGeneric configBtn = addButton(new ButtonSwitch(rightX -= bSize, y, MacroIcons.SETTINGS, module.configure), (_, mouseButton) -> openConfigure(mouseButton, module));
        addCommentForWidget(configBtn, BUTTON_MODULE_CONFIGURE);

        ButtonGeneric enableBtn = addButton(new ButtonSwitch(rightX -= bSize, y, MacroIcons.ENABLED, module.getEnabled().getBooleanValue()), (button1, mouseButton) -> enableOrDisable(button1, mouseButton, module));
        addCommentForWidget(enableBtn, BUTTON_MODULE_ENABLED);

        ButtonGeneric button = addButton(new ButtonSwitch(x, y, MacroIcons.FOLDER, module.isOpen), (button1, mouseButton) -> openOrCloseFolder(button1, mouseButton, module));
        x += bSize;
        addCommentForWidget(button, BUTTON_MODULE_OPEN);

        if (module.configure) {
            int leftWidth = rightX - (getX() + getWidth() / 2) - space();
            if (leftWidth < MIN_BUTTON_SIZE) {
                setHeight((lineHeight + space()) * 2);
                int y1 = getY() + lineHeight + space();

                IHotkey hotkey = module.getGuiKeybind();
                ConfigButtonKeybind keybindButton = new ConfigButtonKeybind(getX(), y1,
                        200, lineHeight - 1, hotkey.getKeybind(), this.host);

                addButton(keybindButton, this.host.getButtonPressListener());
                addCommentForWidget(keybindButton, hotkey.getComment());
            } else {

                IHotkey hotkey = module.getGuiKeybind();
                ConfigButtonKeybind keybindButton = new ConfigButtonKeybind(rightX = getX() + getWidth() / 2, y,
                        leftWidth, lineHeight - 2, hotkey.getKeybind(), this.host);

                addButton(keybindButton, this.host.getButtonPressListener());
                addCommentForWidget(keybindButton, hotkey.getComment());
            }
        }


        int labelWidth = rightX - x - space();

        if (module.configure) {
            addTextField(x, y, labelWidth, lineHeight, module.getNameConfig(), module.getNameConfig().getComment(), 30, false);
        } else {
            String name = module.getNameConfig().getStringValue();
            if (!name.isEmpty()) {
                addLabel(x, this.y, labelWidth, lineHeight, GuiBase.COLOR_WHITE, name);
            }
        }

        addWidget(new ListEntryBox(getX() - 2, y + height - 1, getWidth(), 1, false, true));
    }

    private void openConfigure(int mouseButton, Module module) {
        if (mouseButton == 0) {
            module.configure = !module.configure;
            parent.refreshEntries();
        }
    }
    private void removeThisCollection(int mouseButton, Module module) {
        if (mouseButton == 0) {
            Configs.Macros.Modules.remove(module);
            parent.refreshEntries();
        }
    }


    private void addNewMacro(int mouseButton, Module module) {
        if (mouseButton == 0) {
            module.add(new Macro(module, "Hello world!"));
            parent.refreshEntries();
        }
    }


    private void enableOrDisable(ButtonBase button, int mouseButton, Module module) {
        if (mouseButton == 0) {
            boolean enabled = !module.getEnabled().getBooleanValue();
            module.getEnabled().setBooleanValue(enabled);
            if (button instanceof ButtonSwitch buttonSwitch) buttonSwitch.setOn(enabled);
        }
    }

    private void openOrCloseFolder(ButtonBase button, int mouseButton, Module module) {
        if (mouseButton == 0 && module != null) {
            module.isOpen = !module.isOpen;
            if (button instanceof ButtonSwitch buttonSwitch) buttonSwitch.setOn(!module.isOpen);
            parent.refreshEntries();
        }
    }
}
