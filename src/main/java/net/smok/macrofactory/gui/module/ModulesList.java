package net.smok.macrofactory.gui.module;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.ParentElement;
import net.smok.macrofactory.Configs;
import net.smok.macrofactory.guiold.modules.ModuleWrapper;
import net.smok.macrofactory.gui.container.WidgetList;
import net.smok.macrofactory.gui.container.WidgetListEntry;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.Module;

import java.util.Collection;

public class ModulesList extends WidgetList<ModuleWrapper> {
    public ModulesList(int x, int y, int w, int h, int space, ParentElement parent) {
        super(x, y, w, h, space, parent);
    }

    @Override
    protected WidgetListEntry<ModuleWrapper> createWidgetEntry(ModuleWrapper entry, int entryX, int entryY, int entryWidth, int entryHeight) {
        switch (entry.getType()) {
            case MACRO -> {
                return new MacroEntry(entryX, entryY, entryWidth, entryHeight, entry, this);
            }
            case MODULE -> {
                return new ModuleEntry(entryX, entryY, entryWidth, entryHeight, entry, this);
            }
        }
        return null;
    }

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

}
