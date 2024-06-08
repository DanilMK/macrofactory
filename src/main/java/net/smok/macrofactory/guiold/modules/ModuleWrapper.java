package net.smok.macrofactory.guiold.modules;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import net.smok.macrofactory.guiold.FilteredEntry;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.Module;

import java.util.Collections;
import java.util.List;

public final class ModuleWrapper implements FilteredEntry {
    private final Module module;
    private final Macro macro;
    private final Type type;

    public ModuleWrapper(Module module) {
        this.module = module;
        macro = null;
        type = Type.MODULE;
    }

    public ModuleWrapper(Macro macro) {
        this.macro = macro;
        module = null;
        type = Type.MACRO;
    }

    public Type getType() {
        return type;
    }

    public static Module getModule(ModuleWrapper wrapper) {
        if (wrapper != null && wrapper.type == Type.MODULE) return wrapper.module;
        return null;
    }

    public static Macro getMacro(ModuleWrapper wrapper) {
        if (wrapper != null && wrapper.type == Type.MACRO) return wrapper.macro;
        return null;
    }

    @Override
    public boolean isFiltered(String filterText, IKeybind filterKeys, boolean entryMatchesFilter) {
        switch (type) {

            case MACRO -> {
                return filterText.isEmpty() ? macro != null && macro.getModule().isOpen.getBooleanValue() : entryMatchesFilter;
            }
            case MODULE -> {
                return filterText.isEmpty() || entryMatchesFilter;
            }
        }
        return true;
    }

    @Override
    public List<String> getStringsForFilter() {
        switch (type) {

            case MACRO -> {
                if (macro != null) return ImmutableList.of(macro.getName().toLowerCase(), macro.getCommandAction().getCommand().getStringValue().toLowerCase());

            }
            case MODULE -> {
                if (module != null) return ImmutableList.of(
                        module.getName().toLowerCase(),
                        module.getEnabled().getBooleanValue() ? "enabled" : "disabled"
                );
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isVisibleContent() {
        return type != Type.MACRO || macro != null && macro.getModule().isOpen.getBooleanValue();
    }

    public enum Type {
        MACRO, MODULE
    }
}
