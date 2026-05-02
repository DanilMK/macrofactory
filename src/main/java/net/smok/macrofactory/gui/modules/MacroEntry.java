package net.smok.macrofactory.gui.modules;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.button.*;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;
import net.smok.macrofactory.gui.*;
import net.smok.macrofactory.gui.selector.ItemIconWidget;
import net.smok.macrofactory.gui.utils.ListEntryBox;
import net.smok.macrofactory.macros.CallType;
import net.smok.macrofactory.macros.DeleteAction;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.actions.ActionType;
import net.smok.macrofactory.macros.actions.CommandAction;

public class MacroEntry extends GuiEntry<ModuleWrapper> {

    private static final String BUTTON_MACRO_REMOVE = "gui.button.macro_remove";
    private static final String BUTTON_MACRO_CONFIGURE = "gui.button.macro_configure";
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
        int x = getX();
        int lineHeight = getHeight();
        int buttonHeight = 20;
        int y = getY() + 1;
        int center = x + getWidth() / 2;
        if (macro.configure) y += space();

        {
            int rightX = x + getWidth();
            ButtonGeneric removeButton = addButton(new ButtonGeneric(rightX -= bSize, y, MacroIcons.MACRO_REMOVE), (_, mouseButton1) -> removeMacro(mouseButton1, macro));
            addCommentForWidget(removeButton, BUTTON_MACRO_REMOVE);

            ButtonGeneric configButton = addButton(new ButtonSwitch(rightX -= bSize, y, MacroIcons.SETTINGS, macro.configure), (_, mouseButton) -> openConfigure(mouseButton, macro));
            addCommentForWidget(configButton, BUTTON_MACRO_CONFIGURE);

            ConfigButtonKeybind keybindButton = addKeybindButton(center, y, rightX - center - space(), buttonHeight, macro.getHotkey());
            addCommentForWidget(keybindButton, macro.getHotkey().getComment());
        }

        if (macro.configure) {

            ButtonGeneric iconButton = addButton(new ButtonGeneric(x, y, MacroIcons.MACRO_EMPTY_BUTTON), (_, mouseButton) -> macro.getIcon().setIconFromHand(mouseButton));
            x += bSize;
            addCommentForWidget(iconButton, macro.getIcon().getComment());
            addWidget(new ItemIconWidget(iconButton.getX(), iconButton.getY(), iconButton.getWidth(), iconButton.getHeight(), macro.getIcon(), MacroIcons.MACRO_EMPTY_ICON));

            addTextField(x, y + 1, center - x - space(), buttonHeight, macro.getNameConfig(), macro.getNameConfig().getComment(), 30, true);

            x = getX();
            y += lineHeight;
            int btnWidth = getWidth() / 2 - space();

            addAction(macro, x, y, center, lineHeight, buttonHeight);
            ConfigButtonOptionList optionButton = new ConfigButtonOptionList(center, y, btnWidth, buttonHeight, macro.getActionType());

            addButton(optionButton, this::changeActionType);
            addCommentForWidget(optionButton, macro.getActionType().getComment());

            x = getX();
            y += lineHeight;

            delayText = addTextField(x, y + 1, center - x - space(), buttonHeight, macro.getDelayConfig(), macro.getDelayConfig().getComment(), 4, false);
            ConfigButtonOptionList optionButton1 = new ConfigButtonOptionList(center, y, btnWidth, buttonHeight, macro.getCallType());

            addButton(optionButton1, (_, mouseButton) -> changeCallType(mouseButton, macro));
            addCommentForWidget(optionButton1, macro.getCallType().getComment());


            setHeight(lineHeight * 3 + space() * 3);

        } else {
            if (macro.getIcon().isModified()) {
                addWidget(new ItemIconWidget(x, y, 20, lineHeight, macro.getIcon(), MacroIcons.MACRO_EMPTY_ICON));
                x += bSize;
            }
            String name = macro.getNameConfig().getStringValue();
            if (!name.isEmpty() && (center - x) / 3 > MIN_BUTTON_SIZE) {
                int labelWidth = Math.clamp((center - x) / 3, MIN_BUTTON_SIZE, MAX_BUTTON_SIZE);
                addLabel(x, y, labelWidth, lineHeight, GuiBase.COLOR_WHITE, name);
                x += labelWidth + space();
            }

            addAction(macro, x, y, center, lineHeight, buttonHeight);
        }

        changeButtonsByCallType(macro);
        addWidget(new ListEntryBox(getX() - 2, this.y - 1, 1, getHeight() + 1, true, false));
        addWidget(new ListEntryBox(getX() - 2, this.y + height - 1, getWidth(), 1, false, true));
    }



    private void addAction(Macro macro, int x, int y, int rightX, int lineHeight, int buttonHeight) {
        ActionType actionType = (ActionType) macro.getActionType().getOptionListValue();

        switch (actionType) {

            case Command -> {
                CommandAction config = macro.getCommandAction();
                if (macro.configure) {
                    ButtonGeneric chatButton = addButton(new ButtonSwitch(x, y, MacroIcons.CHAT, config.getInChat().getBooleanValue()), config::switchInChat);
                    addCommentForWidget(chatButton, config.getInChat().getComment());
                    x += bSize;
                    inChatButton = chatButton;
                }
                addTextField(x, y + 1, rightX - x - space(), buttonHeight, config.getCommand(), config.getCommand().getComment(), 256, true);
            }
            case Player -> {
                ConfigButtonOptionList optionButton = new ConfigButtonOptionList(x, y, rightX - x - space(), buttonHeight, macro.getPlayerAction());

                addButton(optionButton, this.host.getButtonPressListener());
                addCommentForWidget(optionButton, macro.getPlayerAction().getComment());
            }
        }
    }


    private void changeCallType(int mouseButton, Macro macro) {
        if (mouseButton == 0) {
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


    private void openConfigure(int mouseButton, Macro macro) {
        if (mouseButton == 0) {
            macro.configure = !macro.configure;
            parent.refreshEntries();
        }
    }

    private void removeMacro(int mouseButton, Macro macro) {
        if (mouseButton == 0) {
            if (macro.isModified()) {
                macro.markAsDeleted = true;
                ModulesGui.deleteStack.push(new DeleteAction(macro, null));
            }
            else if (macro.getModule() != null) macro.getModule().remove(macro);
            parent.refreshEntries();
        }
    }


}
