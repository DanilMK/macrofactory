package net.smok.macrofactory.gui.module;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.smok.macrofactory.Configs;
import net.smok.macrofactory.guiold.MacroIcons;
import net.smok.macrofactory.guiold.modules.ModuleWrapper;
import net.smok.macrofactory.guiold.utils.ListEntryBox;
import net.smok.macrofactory.gui.base.*;
import net.smok.macrofactory.gui.config.ConfigBooleanButton;
import net.smok.macrofactory.gui.config.ConfigKeybindButton;
import net.smok.macrofactory.gui.container.FixedContainer;
import net.smok.macrofactory.gui.container.WidgetList;
import net.smok.macrofactory.gui.container.WidgetListEntry;
import net.smok.macrofactory.gui.config.ConfigStringWidget;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.Module;

public class ModuleEntry extends WidgetListEntry<ModuleWrapper> {


    private static final String BUTTON_MACRO_ADD = "guiold.button.macro_add";
    private static final String BUTTON_MODULE_REMOVE = "guiold.button.module_remove";

    public ModuleEntry(int x, int y, int w, int h, ModuleWrapper entry, WidgetList<ModuleWrapper> parent) {
        super(x, y, w, h, entry, parent);

        Module module = ModuleWrapper.getModule(entry);
        if (module == null) return;

        FixedContainer container = new FixedContainer(x, y, w, h, this, 3, false, true);



        container.add(new ButtonBase(MacroIcons.FOLDER_REMOVE, button -> removeThisCollection(module))).setTooltip(Tooltip.of(Text.translatable(BUTTON_MODULE_REMOVE)));
        container.add(new ButtonBase(MacroIcons.MACRO_ADD, button -> addNewMacro(module))).setTooltip(Tooltip.of(Text.translatable(BUTTON_MACRO_ADD)));
        container.add(new ConfigBooleanButton(MacroIcons.CONFIGURE, MacroIcons.CONFIGURE_OFF, module.getIsConfigure(), button -> {
            parent.refreshPositions();
        /*
        widgetEntries.clear();
        nextPosition = getEntryStartY();
        MacroFactory.LOGGER.info("Refresh entries");

        if (widgetSearchBar == null || !widgetSearchBar.hasFilter()) addNonFilteredContents(this::createWidgetEntry);
        else addFilteredContents(this::createWidgetEntry);*/
        }));
        container.add(new ConfigBooleanButton(MacroIcons.ENABLED, MacroIcons.ENABLED_OFF, module.getEnabled(), button -> {
            parent.refreshPositions();
        /*
        widgetEntries.clear();
        nextPosition = getEntryStartY();
        MacroFactory.LOGGER.info("Refresh entries");

        if (widgetSearchBar == null || !widgetSearchBar.hasFilter()) addNonFilteredContents(this::createWidgetEntry);
        else addFilteredContents(this::createWidgetEntry);*/
        }));



        if (module.getIsConfigure().getBooleanValue()) {
            container.add(new ConfigKeybindButton(120, getHeight(), module.getGuiKeybind()));

            container.add(new ConfigStringWidget(container.emptySpace() - MacroIcons.FOLDER.getWidth() - container.getSpace(), h, module.getNameConfig(), 30));
        } else {
            String name = module.getNameConfig().getStringValue();
            if (!name.isEmpty()) {

                container.add(new LabelWidget(x, y, container.emptySpace() - MacroIcons.FOLDER.getWidth() - container.getSpace(), h, name));
            }
        }

        container.add(new ConfigBooleanButton(MacroIcons.FOLDER, MacroIcons.FOLDER_OFF, module.getIsOpen(), button -> {
            parent.refreshPositions();
        /*
        widgetEntries.clear();
        nextPosition = getEntryStartY();
        MacroFactory.LOGGER.info("Refresh entries");

        if (widgetSearchBar == null || !widgetSearchBar.hasFilter()) addNonFilteredContents(this::createWidgetEntry);
        else addFilteredContents(this::createWidgetEntry);*/
        }));


        addDrawableElement(container);
        addDrawable(new ListEntryBox(x - 2, y + getHeight() + 1, getWidth(), 1, false, true));
    }


    private void removeThisCollection(Module module) {
        Configs.Macros.Modules.remove(module);
        parent.refreshPositions();
        /*
        widgetEntries.clear();
        nextPosition = getEntryStartY();
        MacroFactory.LOGGER.info("Refresh entries");

        if (widgetSearchBar == null || !widgetSearchBar.hasFilter()) addNonFilteredContents(this::createWidgetEntry);
        else addFilteredContents(this::createWidgetEntry);*/
    }


    private void addNewMacro(Module module) {
        module.add(new Macro(module, "Hello world!"));
        parent.refreshPositions();
        /*
        widgetEntries.clear();
        nextPosition = getEntryStartY();
        MacroFactory.LOGGER.info("Refresh entries");

        if (widgetSearchBar == null || !widgetSearchBar.hasFilter()) addNonFilteredContents(this::createWidgetEntry);
        else addFilteredContents(this::createWidgetEntry);*/
    }

}
