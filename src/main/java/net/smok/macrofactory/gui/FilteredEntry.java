package net.smok.macrofactory.gui;

import fi.dy.masa.malilib.config.ConfigType;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigResettable;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface FilteredEntry {

    boolean isFiltered(String filterText, IKeybind filterKeys, boolean entryMatchesFilter);
    default boolean isVisibleContent() {
        return true;
    } 

    List<String> getStringsForFilter();

    @NotNull
    static FilteredEntry convert(Object entry) {
        FilteredEntry filteredEntry;
        if (entry instanceof FilteredEntry) filteredEntry = (FilteredEntry) entry;
        else if (entry instanceof IConfigBase configEntry) filteredEntry = FilteredEntry.convert(configEntry);
        else if (entry instanceof GuiConfigsBase.ConfigOptionWrapper wrapperEntry) filteredEntry = FilteredEntry.convert(wrapperEntry);
        else return empty();
        return filteredEntry;
    }

    @NotNull
    static FilteredEntry convert(@NotNull GuiConfigsBase.ConfigOptionWrapper wrapper) {
        return wrapper.getConfig() != null ? convert(wrapper.getConfig()) : empty();
    }

    @NotNull
    static FilteredEntry empty() {
        return new FilteredEntry() {
            @Override
            public boolean isFiltered(String filterText, IKeybind filterKeys, boolean entryMatchesFilter) {
                return filterText.isEmpty() || entryMatchesFilter;
            }

            @Override
            public List<String> getStringsForFilter() {
                return Collections.emptyList();
            }
        };
    }

    @NotNull
    static FilteredEntry convert(@NotNull IConfigBase config) {
        return new FilteredEntry() {
            @Override
            public boolean isFiltered(String filterText, IKeybind filterKeys, boolean entryMatchesFilter) {
                return filterText.isEmpty() || entryMatchesFilter &&
                        (config.getType() != ConfigType.HOTKEY ||
                        filterKeys.getKeys().isEmpty() ||
                        ((IHotkey) config).getKeybind().overlaps(filterKeys));
            }

            @Override
            public List<String> getStringsForFilter() {

                ArrayList<String> list = new ArrayList<>();
                String name = config.getName();
                String translated = config.getConfigGuiDisplayName();

                list.add(name.toLowerCase());

                if (!name.equals(translated))
                {
                    list.add(translated.toLowerCase());
                }

                if (config instanceof IConfigResettable && ((IConfigResettable) config).isModified())
                {
                    list.add("modified");
                }

                return list;
            }
        };
    }
}
