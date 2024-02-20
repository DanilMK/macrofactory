package net.smok.macrofactory.gui.modules;

import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;
import net.smok.macrofactory.gui.*;
import net.smok.macrofactory.gui.utils.SwitchComment;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.gui.utils.ListEntryBox;
import net.smok.macrofactory.macros.actions.ActionType;

public class MacroEntry extends GuiEntry<ModuleWrapper> {

    private static final String COMMENT_MACRO_REMOVE = "gui.button.macro_remove";
    private static final String COMMENT_MACRO_CONFIGURE = "gui.button.macro_configure";
    private static final String COMMENT_CHAT = "gui.button.chat";



    public MacroEntry(int x, int y, int width, int lineHeight, int space, GuiList<?, ?> parent, ModuleWrapper entry,
                      int listIndex, IKeybindConfigGui host) {
        super(x, y, width, lineHeight, space, parent, entry, listIndex, host);
    }

    @Override
    public void init() {
        Macro macro = ModuleWrapper.getMacro(entry);

        if (macro == null) return;

        addGenericButton(new PositionAlignment(false, MacroIcons.MACRO_REMOVE), MacroIcons.MACRO_REMOVE, this::removeMacro, COMMENT_MACRO_REMOVE);
        addSwitchButton(new PositionAlignment(false, MacroIcons.SETTINGS), MacroIcons.SETTINGS, macro.configure, this::openConfigure, COMMENT_MACRO_CONFIGURE);
        addKeybindButton(new PositionAlignment(false, 120), macro.getHotkey(), macro.getHotkey().getComment());


        if (macro.configure) {
            addTextField(new PositionAlignment(true, 120), macro.getNameConfig(), 30);
        } else {
            String name = macro.getNameConfig().getStringValue();
            if (!name.isEmpty()) {
                addLabel(new PositionAlignment(true, 115, 5), GuiBase.COLOR_WHITE, name);
            }
        }

        ActionType actionType = (ActionType) macro.getActionType().getOptionListValue();

        switch (actionType) {

            case Command -> {
                addSwitchButton(false, MacroIcons.CHAT, macro.isChat(), this::setChat, COMMENT_CHAT);
                IConfigValue config = macro.getCommandAction();
                addTextField(new PositionAlignment(true), config, 256);
            }
            case Player -> {
                addOptionListButton(new PositionAlignment(true, 200), macro.getPlayerAction(), this.host.getButtonPressListener(), macro.getPlayerAction().getComment());
            }
        }

        if (macro.configure) {
            addLine();

            addTextField(new PositionAlignment(false, 40 + space()), macro.getCoolDownConfig(), 4);
            addOptionListButton(new PositionAlignment(false, 120), macro.getCallType(), this.host.getButtonPressListener());
            addOptionListButton(new PositionAlignment(true, 200), macro.getActionType(), this::changeActionType);
            addWidget(new ListEntryBox(x - 2, y + height - 1, getWidth(), 1, false, true));
        }

        addWidget(new ListEntryBox(x - 2, y - 1, 1, getHeight() + 1, true, false));

    }

    private void setChat(ButtonBase button, int mouseButton) {
        Macro macro = ModuleWrapper.getMacro(entry);
        if (mouseButton == 0 && macro != null) {
            macro.setInChat(!macro.isChat());

            if (button instanceof ButtonSwitch buttonSwitch) buttonSwitch.setOn(macro.isChat());
        }
    }



    private void changeActionType(ButtonBase button, int mouseButton) {

        parent.refreshEntries();
    }


    private void openConfigure(ButtonBase button, int mouseButton) {
        Macro macro = ModuleWrapper.getMacro(entry);
        if (mouseButton == 0 && macro != null) {
            macro.configure = !macro.configure;
            parent.refreshEntries();
        }
    }

    private void removeMacro(ButtonBase button, int mouseButton) {
        Macro macro = ModuleWrapper.getMacro(entry);
        if (mouseButton == 0 && macro != null) {
            if (macro.getModule() != null) macro.getModule().remove(macro);
            parent.refreshEntries();
        }
    }


}
