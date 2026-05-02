package net.smok.macrofactory.gui.modules;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.widgets.WidgetHoverInfo;
import fi.dy.masa.malilib.gui.widgets.WidgetLabel;
import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.screens.Screen;
import net.smok.macrofactory.Configs;
import net.smok.macrofactory.gui.*;
import net.smok.macrofactory.macros.DeleteAction;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.Module;

import java.util.Collection;
import java.util.Objects;
import java.util.Stack;

public class ModulesGui extends GuiScreen<ModuleWrapper, GuiEntry<ModuleWrapper>> {


    public static final Stack<DeleteAction> deleteStack = new Stack<>();
    private ButtonGeneric deleteButton;

    public ModulesGui(Screen parent) {
        super(10, 25, "gui.title.screen_module");
        setParent(parent);
        deleteStack.clear();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        reCreateListWidget();
        Objects.requireNonNull(getListWidget()).refreshEntries();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();


        int right = getBrowserX() + getBrowserWidth();
        IGuiIcon folderIcon = MacroIcons.FOLDER_ADD;
        int folderX = right - folderIcon.getWidth();
        int folderY = 25;
        ButtonGeneric buttonAdd = addButton(new ButtonGeneric(folderX, folderY, folderIcon), (_, mouseButton) -> {
            if (mouseButton == 0) {
                Configs.Macros.Modules.add(new Module("Mew Module", true));
                if (getListWidget() != null) getListWidget().refreshEntries();
            }
        });
        addWidget(new WidgetHoverInfo(buttonAdd.getX(), buttonAdd.getY(), buttonAdd.getWidth(), buttonAdd.getHeight(), "gui.button.module_add"));

        int keybindWidth = 100;
        int keybindHeight = 20;
        int keybindX = right - keybindWidth;
        int keybindY = 3;

        addWidget(new WidgetLabel(keybindX - 100, keybindY, 100, keybindHeight, 0xffffffff, Configs.Generic.CMD_MACRO_OPEN.getTranslatedName()));

        addWidget(new ConfigButtonKeybind(keybindX, keybindY, keybindWidth, keybindHeight,
                Configs.Generic.CMD_MACRO_OPEN.getKeybind(), this));
        addWidget(new WidgetHoverInfo(keybindX, keybindY, keybindWidth, keybindHeight, Configs.Generic.CMD_MACRO_OPEN.getComment()));

        addWidget(new ButtonGenericWithoutScroll(width / 2 - 205, getListY() + getBrowserHeight() + 20, 200, false, "gui.done")
                .setActionListener((_, mouseButton) -> {
                    if (mouseButton == 0) closeGui(true);
                }));

        deleteButton = addWidget(new ButtonGenericWithoutScroll(width / 2 + 5, getListY() + getBrowserHeight() + 20, 200, false, "gui.button.undo_delete")
                .setActionListener((_, mouseButton) -> {
                    if (mouseButton == 0) {
                        deleteStack.pop().undo();
                        getListWidget().refreshEntries();
                    }
                }));
        refreshDeleteButton();

    }

    @Override
    protected GuiList<ModuleWrapper, GuiEntry<ModuleWrapper>> createListWidget(int listX, int listY) {
        ModulesGui gui = this;
        return new GuiList<>(width / 2 - getBrowserWidth() / 2, listY, getBrowserWidth(), getBrowserHeight(), false, 22, getBrowserWidth() - 23) {
            @Override
            public Collection<ModuleWrapper> getAllEntries() {
                ImmutableList.Builder<ModuleWrapper> builder = ImmutableList.builder();

                for (Module module : Configs.Macros.Modules) {
                    if (module.markAsDeleted) continue;
                    builder.add(new ModuleWrapper(module));

                    for (Macro macro : module.getAll())
                        if (!macro.markAsDeleted) builder.add(new ModuleWrapper(macro));
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

            @Override
            public void refreshEntries() {
                super.refreshEntries();
                refreshDeleteButton();
            }
        };
    }

    @Override
    protected void drawTitle(GuiContext ctx, int mouseX, int mouseY, float partialTicks) {
        this.drawString(ctx, this.getTitleString(), getBrowserX(), TOP, COLOR_WHITE);
    }

    public void refreshDeleteButton() {
        if (deleteButton != null) deleteButton.setEnabled(!deleteStack.empty());
    }
}
