package net.smok.macrofactory.gui.modules;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiDialogBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.interfaces.IDialogHandler;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.widgets.WidgetHoverInfo;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.smok.macrofactory.Configs;
import net.smok.macrofactory.ModulesKeybindProvider;
import net.smok.macrofactory.gui.GuiEntry;
import net.smok.macrofactory.gui.GuiList;
import net.smok.macrofactory.gui.GuiScreen;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.Module;
import net.smok.macrofactory.gui.MacroIcons;

import java.util.Collection;

public class ModulesGui extends GuiScreen<ModuleWrapper, GuiEntry<ModuleWrapper>> {


    public ModulesGui(Screen parent) {
        super(10, 25, "gui.title.screen_module");
        setParent(parent);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();


        IGuiIcon folderIcon = MacroIcons.FOLDER_ADD;
        int folderX = 10 + getBrowserWidth() - folderIcon.getWidth();
        int folderY = 25;
        ButtonGeneric buttonAdd = addButton(new ButtonGeneric(folderX, folderY, folderIcon), (button, mouseButton) -> {
            if (mouseButton == 0) {
                Configs.Macros.Modules.add(new Module("Mew Module", true));
                if (getListWidget() != null) getListWidget().refreshEntries();
            }
        });
        addWidget(new WidgetHoverInfo(buttonAdd.getX(), buttonAdd.getY(), buttonAdd.getWidth(), buttonAdd.getHeight(), "gui.button.module_add"));

        int keybindWidth = 100;
        int keybindHeight = 20;
        int keybindX = 10 + getBrowserWidth() - keybindWidth;
        int keybindY = 3;

        addWidget(new ConfigButtonKeybind(keybindX, keybindY, keybindWidth, keybindHeight,
                Configs.Generic.CMD_MACRO_OPEN.getKeybind(), this));
        addWidget(new WidgetHoverInfo(keybindX, keybindY, keybindWidth, keybindHeight, Configs.Generic.CMD_MACRO_OPEN.getComment()));


    }

    @Override
    protected GuiList<ModuleWrapper, GuiEntry<ModuleWrapper>> createListWidget(int listX, int listY) {
        ModulesGui gui = this;
        return new GuiList<>(listX, listY, getBrowserWidth(), getBrowserHeight(), false, 22, getBrowserWidth() - 23) {
            @Override
            public Collection<ModuleWrapper> getAllEntries() {
                ImmutableList.Builder<ModuleWrapper> builder = ImmutableList.builder();

                for (Module module : Configs.Macros.Modules) {
                    builder.add(new ModuleWrapper(module));

                    for (Macro macro : module.getAll())
                        builder.add(new ModuleWrapper(macro));
                }
                return builder.build();
            }

            @Override
            protected GuiEntry<ModuleWrapper> createListEntryWidget(int x, int y, int listIndex, boolean isOdd, ModuleWrapper entry) {
                switch (entry.getType()) {

                    case MACRO -> {
                        return new MacroEntry(x, y, browserEntryWidth, browserEntryHeight, 3, this, entry, listIndex, gui);
                    }
                    case MODULE -> {
                        return new ModuleEntry(x, y, browserEntryWidth, browserEntryHeight, 3, this, entry, listIndex, gui);
                    }
                }
                return null;
            }
        };
    }

    @Override
    protected void closeGui(boolean showParent) {
        super.closeGui(showParent);


        ModulesKeybindProvider.update();
    }


    /*
    @Override
    protected int getBrowserWidth() {
        return Math.min(super.getBrowserWidth(), 1000);
    }*/
}
