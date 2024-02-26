package net.smok.macrofactory.gui;

import fi.dy.masa.malilib.gui.LeftRight;
import fi.dy.masa.malilib.gui.MaLiLibIcons;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;
import fi.dy.masa.malilib.gui.widgets.WidgetSearchBar;
import fi.dy.masa.malilib.gui.widgets.WidgetSearchBarConfigs;
import fi.dy.masa.malilib.hotkeys.IKeybind;

import java.util.Collection;
import java.util.List;

public abstract class GuiList<T, W extends GuiEntry<T>> extends WidgetListConfigOptionsBase<T, W> {

    protected final WidgetSearchBarConfigs widgetSearchConfigs;

    public GuiList(int x, int y, int width, int height, boolean useKeybindSearch, int entryHeight, int searchBarWidth) {
        super(x, y, width, height, 100);
        this.browserEntryHeight = entryHeight;
        this.entryHeight = entryHeight;

        if (useKeybindSearch)
        {
            this.widgetSearchConfigs = new WidgetSearchBarConfigs(x + 2, y + 4, searchBarWidth, 20, 0, MaLiLibIcons.SEARCH, LeftRight.LEFT);
            this.widgetSearchBar = this.widgetSearchConfigs;
            this.browserEntriesOffsetY = 23;
        }
        else
        {
            this.widgetSearchConfigs = null;
            this.widgetSearchBar = new WidgetSearchBar(x + 2, y + 4, searchBarWidth, 14, 0, MaLiLibIcons.SEARCH, LeftRight.LEFT);
            this.browserEntriesOffsetY = 17;
        }
    }

    @Override
    public abstract Collection<T> getAllEntries();

    @Override
    protected W createListEntryWidgetIfSpace(int x, int y, int listIndex, int usableHeight, int usedHeight) {
        W widget = super.createListEntryWidgetIfSpace(x, y, listIndex, usableHeight, usedHeight);
        if (widget != null) widget.init();
        return widget;
    }

    @Override
    protected List<String> getEntryStringsForFilter(T entry) {
        return FilteredEntry.convert(entry).getStringsForFilter();
    }

    @Override
    protected void addFilteredContents(Collection<T> entries) {
        if (this.widgetSearchConfigs == null) {
            super.addFilteredContents(entries);
            return;
        }

        String filterText = this.widgetSearchConfigs.getFilter();
        IKeybind filterKeys = this.widgetSearchConfigs.getKeybind();

        for (T entry : entries)
            if (FilteredEntry.convert(entry).isFiltered(filterText, filterKeys, entryMatchesFilter(entry, filterText)))
                this.listContents.add(entry);

    }

    @Override
    protected void addNonFilteredContents(Collection<T> placements) {
        for (T entry : placements) if (FilteredEntry.convert(entry).isVisibleContent()) listContents.add(entry);
    }

}
