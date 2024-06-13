package net.smok.macrofactory.guiold.modules;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;
import net.smok.macrofactory.guiold.GuiEntry;
import net.smok.macrofactory.guiold.GuiList;
import net.smok.macrofactory.gui.MacroIcons;
import net.smok.macrofactory.guiold.PositionAlignment;
import net.smok.macrofactory.guiold.selector.ItemIconWidget;
import net.smok.macrofactory.guiold.utils.ListEntryBox;
import net.smok.macrofactory.macros.CallType;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.actions.ActionType;
import net.smok.macrofactory.macros.actions.CommandAction;

public class MacroEntry extends GuiEntry<ModuleWrapper> {

    private static final String BUTTON_MACRO_REMOVE = "guiold.button.macro_remove";
    private static final String BUTTON_MACRO_CONFIGURE = "guiold.button.macro_configure";
    private ButtonGeneric inChatButton;
    private GuiTextFieldGeneric delayText;


    public MacroEntry(int x, int y, int width, int lineHeight, int space, GuiList<?, ?> parent, ModuleWrapper entry,
                      int listIndex, IKeybindConfigGui host) {
        super(x, y, width, lineHeight, space, parent, entry, listIndex, host);
    }

    @Override
    public void init() {
        Macro macro = ModuleWrapper.getMacro(entry);

        if (macro == null) return;

        addGenericButton(new PositionAlignment(false, MacroIcons.MACRO_REMOVE), MacroIcons.MACRO_REMOVE, this::removeMacro, BUTTON_MACRO_REMOVE);
        addSwitchButton(new PositionAlignment(false, MacroIcons.CONFIGURE), MacroIcons.CONFIGURE, macro.configure, this::openConfigure, BUTTON_MACRO_CONFIGURE);
        addKeybindButton(new PositionAlignment(false, 120), macro.getHotkey());


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
                CommandAction config = macro.getCommandAction();
                inChatButton = addSwitchButton(false, MacroIcons.CHAT, config.getInChat().getBooleanValue(), config::switchInChat, config.getInChat().getComment());
                addTextField(new PositionAlignment(true), config.getCommand(), 256);
            }
            case Player -> addOptionListButton(new PositionAlignment(true, 200), macro.getPlayerAction(), this.host.getButtonPressListener(), macro.getPlayerAction().getComment());
        }

        if (macro.configure) {
            addLine();

            ButtonGeneric iconButton = addGenericButton(true, MacroIcons.MACRO_EMPTY_BUTTON, (buttonBase, mouseButton) -> macro.getIcon().setIconFromHand(), macro.getIcon().getComment());
            addWidget(new ItemIconWidget(iconButton.getX(), iconButton.getY(), iconButton.getWidth(), iconButton.getHeight(), macro.getIcon(), MacroIcons.MACRO_ICON));
            delayText = addTextField(new PositionAlignment(false, 40 + space()), macro.getDelayConfig(), 4);
            addOptionListButton(new PositionAlignment(false, 120), macro.getCallType(), this::changeCallType);
            addOptionListButton(new PositionAlignment(true, 200), macro.getActionType(), this::changeActionType);
            addWidget(new ListEntryBox(x - 2, y + height - 1, getWidth(), 1, false, true));
        }

        changeButtonsByCallType(macro);
        addWidget(new ListEntryBox(x - 2, y - 1, 1, getHeight() + 1, true, false));

    }


    private void changeCallType(ButtonBase buttonBase, int mouseButton) {
        Macro macro = ModuleWrapper.getMacro(entry);
        if (mouseButton == 0 && macro != null) {
            changeButtonsByCallType(macro);
        }
    }

    private void changeButtonsByCallType(Macro macro) {
        boolean single = macro.getCallType().getOptionListValue() == CallType.SINGLE;
        if (inChatButton != null) {
            inChatButton.setEnabled(single);
            if (!single) macro.getCommandAction().getInChat().setBooleanValue(false);
        }
        if (delayText != null)  {
            delayText.setEditable(!single);
            if (single) delayText.setFocused(false);
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
