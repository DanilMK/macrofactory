package net.smok.macrofactory.gui.module;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.smok.macrofactory.guiold.MacroIcons;
import net.smok.macrofactory.guiold.modules.ModuleWrapper;
import net.smok.macrofactory.guiold.selector.ItemIconWidget;
import net.smok.macrofactory.guiold.utils.ListEntryBox;
import net.smok.macrofactory.gui.base.ButtonBase;
import net.smok.macrofactory.gui.base.LabelWidget;
import net.smok.macrofactory.gui.config.*;
import net.smok.macrofactory.gui.container.FixedContainer;
import net.smok.macrofactory.gui.container.WidgetList;
import net.smok.macrofactory.gui.container.WidgetListEntry;
import net.smok.macrofactory.macros.CallType;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.actions.ActionType;
import net.smok.macrofactory.macros.actions.CommandAction;
import org.jetbrains.annotations.NotNull;

public class MacroEntry extends WidgetListEntry<ModuleWrapper> {

    private static final String BUTTON_MACRO_REMOVE = "guiold.button.macro_remove";
    private ButtonBase inChatButton;
    private ConfigStringWidget delayText;
    public MacroEntry(int x, int y, int w, int h, ModuleWrapper entry, @NotNull WidgetList<ModuleWrapper> parent) {
        super(x, y, w, h, entry, parent);

        Macro macro = ModuleWrapper.getMacro(entry);
        if (macro == null) return;
        boolean configure = macro.getIsConfigure().getBooleanValue();

        int s = 3;
        setHeight(configure ? h * 2 + parent.getSpace() : h);
        FixedContainer verticalContainer = new FixedContainer(x, y, w, getHeight(), this, 3, true, false);
        FixedContainer topContainer = verticalContainer.add(new FixedContainer(x, y, w, h, this, s, false, true));
        addDrawableElement(verticalContainer);


        topContainer.add(new ButtonBase(MacroIcons.MACRO_REMOVE, button -> {
            macro.getModule().remove(macro);
            parent.refreshPositions();
        /*
        widgetEntries.clear();
        nextPosition = getEntryStartY();
        MacroFactory.LOGGER.info("Refresh entries");

        if (widgetSearchBar == null || !widgetSearchBar.hasFilter()) addNonFilteredContents(this::createWidgetEntry);
        else addFilteredContents(this::createWidgetEntry);*/
        })).setTooltip(Tooltip.of(Text.translatable(BUTTON_MACRO_REMOVE)));
        topContainer.add(new ConfigBooleanButton(MacroIcons.CONFIGURE, MacroIcons.CONFIGURE_OFF, macro.getIsConfigure(), button -> {
            parent.refreshPositions();
        /*
        widgetEntries.clear();
        nextPosition = getEntryStartY();
        MacroFactory.LOGGER.info("Refresh entries");

        if (widgetSearchBar == null || !widgetSearchBar.hasFilter()) addNonFilteredContents(this::createWidgetEntry);
        else addFilteredContents(this::createWidgetEntry);*/
        }));
        topContainer.add(new ConfigKeybindButton(120, h, macro.getHotkey()));




        // Prepare
        String name = macro.getNameConfig().getStringValue();
        int nameWidth = configure || !name.isEmpty() ? 120 : 0;
        int actionWidth = topContainer.emptySpace() - nameWidth - s;


        switch ((ActionType) macro.getActionType().getOptionListValue()) {

            case Command -> {
                CommandAction config = macro.getCommandAction();
                inChatButton = topContainer.add(new ConfigBooleanButton(MacroIcons.CHAT, MacroIcons.CHAT_OFF, config.getInChat(), button -> changeButtonsByCallType(macro)));
                topContainer.add(new ConfigStringWidget(actionWidth - inChatButton.getWidth() - s, h, config.getCommand(), 256));
            }
            case Player -> topContainer.add(new ConfigOptionListButton(actionWidth, h, macro.getPlayerAction()));
        }


        if (configure) topContainer.add(new ConfigStringWidget(nameWidth, h, macro.getNameConfig(), 30));
        else if (!name.isEmpty()) topContainer.add(new LabelWidget(x, y, nameWidth - 5, h, name));

        if (configure) {
            FixedContainer downContainer = verticalContainer.add(new FixedContainer(x, y, w, h, this, 3, false, false));


            ButtonBase iconButton = downContainer.add(new ButtonBase(MacroIcons.MACRO_EMPTY_BUTTON, button -> macro.getIcon().setIconFromHand()));
            iconButton.setTooltip(Tooltip.of(Text.translatable(macro.getIcon().getComment())));

            downContainer.addDrawable(new ItemIconWidget(iconButton.getX(), iconButton.getY(), iconButton.getWidth(), iconButton.getHeight(), macro.getIcon(), MacroIcons.MACRO_EMPTY_ICON));
            downContainer.add(new ConfigOptionListButton(120, h, macro.getCallType(), button -> changeButtonsByCallType(macro)));
            downContainer.add(new ConfigOptionListButton(200, h, macro.getActionType(), button -> parent.refreshPositions()));
            delayText = downContainer.add(new ConfigStringWidget(40, h, macro.getDelayConfig(), 4));
            addDrawable(new ListEntryBox(x - 2, y + getHeight() + 1, getWidth(), 1, false, true));
        }

        addDrawable(new ListEntryBox(x - 2, y, 1, getHeight() + 1, true, false));

        changeButtonsByCallType(macro);
    }

    private void changeButtonsByCallType(Macro macro) {
        boolean single = macro.getCallType().getOptionListValue() == CallType.SINGLE;
        if (inChatButton != null) {
            inChatButton.active = single;
            if (!single) macro.getCommandAction().getInChat().setBooleanValue(false);
        }
        if (delayText != null)  {
            delayText.setEditable(!single);
            if (single) delayText.setFocused(false);
        }
    }
}
