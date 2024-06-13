package net.smok.macrofactory.gui.container;

import fi.dy.masa.malilib.gui.LeftRight;
import fi.dy.masa.malilib.gui.MaLiLibIcons;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.widgets.WidgetSearchBar;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.Text;
import net.smok.macrofactory.MacroFactory;
import net.smok.macrofactory.gui.base.WidgetBase;
import net.smok.macrofactory.guiold.FilteredEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class WidgetList<T extends FilteredEntry> extends WidgetBase implements ParentContainer {


    private final List<WidgetListEntry<T>> widgetEntries = new ArrayList<>();
    private final ParentElement parent;
    private final ScrollableContainer container;
    private int space;
    private WidgetSearch widgetSearchBar;
    private int browserEntriesOffsetY;

    private Element focused;
    private boolean dragging;

    public WidgetList(int x, int y, int w, int h, int space, ParentElement parent) {
        super(x, y, w, h);
        this.parent = parent;
        this.space = space;
        this.container = new ScrollableContainer(x, y, w, h, Text.empty(), 1, space, parent);

        refreshPositions();
    }

    @Override
    public @NotNull ParentElement getParent() {
        return parent;
    }

    @Override
    public List<Element> children() {
        ArrayList<Element> result = new ArrayList<>();
        if (widgetSearchBar != null) result.add(widgetSearchBar);
        result.add(container);
        return result;
    }

    @Override
    public List<Drawable> drawables() {
        ArrayList<Drawable> result = new ArrayList<>();
        if (widgetSearchBar != null) result.add(widgetSearchBar);
        result.add(container);
        return result;
    }

    @Override
    public <C extends Element> Element addElement(@NotNull C elementChild) {
        return elementChild;
    }

    @Override
    public <C extends Drawable> Drawable addDrawable(@NotNull C drawableChild) {
        return drawableChild;
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return super.getNavigationFocus();
    }





    @Nullable
    @Override
    public Element getFocused() {
        return focused;
    }

    @Override
    public void setFocused(Element focused) {
        this.focused = focused;
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public void setSearchConfigs(boolean useKeybind) {
        setSearchConfigs(useKeybind, getEntryWidth());
    }

    public void setSearchConfigs(boolean useKeybind, int searchBarWidth) {

        if (useKeybind) {

            //this.widgetSearchBar = new WidgetSearchBarConfigs(getX() + 2, getY() + 4, searchBarWidth, 20, 0, MaLiLibIcons.SEARCH, LeftRight.LEFT);
            this.browserEntriesOffsetY = 23;

        } else {

            this.widgetSearchBar = new WidgetSearch(getX() + 2, getY() + 4, searchBarWidth, 14, 0, MaLiLibIcons.SEARCH, LeftRight.LEFT);
            this.browserEntriesOffsetY = 17;
        }
    }

    protected int getEntryStartX() {
        return getX() + 1;
    }

    protected int getEntryStartY() {
        return getY() + browserEntriesOffsetY;
    }

    protected static int getEntryHeight() {
        return 20;
    }

    protected int getEntryWidth() {
        return container.getWidth() - 2;
    }




    // Creating content
    public void refreshEntries() {
        widgetEntries.clear();
        container.clear();
        MacroFactory.LOGGER.info("Refresh entries");

        if (widgetSearchBar == null || !widgetSearchBar.hasFilter()) addNonFilteredContents(this::createWidgetEntry);
        else addFilteredContents(this::createWidgetEntry);
    }

    public void refreshPositions() {
        Map<T, WidgetListEntry<T>> oldEntries = widgetEntries.stream().collect(Collectors.toMap(WidgetListEntry::getEntry, entry -> entry));
        widgetEntries.clear();
        container.clear();
        MacroFactory.LOGGER.info("Refresh positions");

        if (widgetSearchBar == null || !widgetSearchBar.hasFilter()) addNonFilteredContents(t -> refreshWidgetEntry(t, oldEntries));
        else addFilteredContents(t -> refreshWidgetEntry(t, oldEntries));

    }

    private void refreshWidgetEntry(T t, Map<T, WidgetListEntry<T>> oldEntries) {
        if (oldEntries.containsKey(t)) {
            WidgetListEntry<T> entry = oldEntries.get(t);
            oldEntries.remove(t);
            if (entry == null) return;
            widgetEntries.add(entry);
            container.add(entry);
        } else createWidgetEntry(t);
    }


    protected void addNonFilteredContents(Consumer<T> consumer) {
        for (T entry : getAllEntries()) {
            if (entry.isVisibleContent()) {
                consumer.accept(entry);
            }
        }
    }

    protected void addFilteredContents(Consumer<T> consumer) {

        String filterText = this.widgetSearchBar.getFilter();
        IKeybind filterKeys = null;
        /*if (widgetSearchBar instanceof WidgetSearchBarConfigs searchBarConfigs)
            filterKeys = searchBarConfigs.getKeybind();*/

        for (T entry : getAllEntries()) {
            if (entry.isFiltered(filterText, filterKeys, matchFilter(entry, filterText))) {
                consumer.accept(entry);
            }
        }
    }

    protected boolean matchFilter(T entry, String text) {
        List<String> strings = entry.getStringsForFilter();

        if (text.isEmpty()) return true;

        for (String s : text.split("\\|")) {
            if (strings.contains(s)) return true;
        }

        return false;
    }


    private void createWidgetEntry(T entry) {
        if (entry == null) return;
        WidgetListEntry<T> widgetEntry = createWidgetEntry(entry, container.getContainer().nextX(), container.getContainer().nextY(), getEntryWidth(), getEntryHeight());
        if (widgetEntry == null) return;
        widgetEntries.add(widgetEntry);
        container.add(widgetEntry);
    }

    protected abstract WidgetListEntry<T> createWidgetEntry(T entry, int entryX, int entryY, int entryWidth, int entryHeight);

    protected abstract Collection<T> getAllEntries();



    // Wrapper for Searching
    private static class WidgetSearch extends WidgetSearchBar implements Drawable, Selectable, Element {

        public WidgetSearch(int x, int y, int width, int height, int searchBarOffsetX, IGuiIcon iconSearch, LeftRight iconAlignment) {
            super(x, y, width, height, searchBarOffsetX, iconSearch, iconAlignment);
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            super.render(mouseX, mouseY, false, context);
        }

        @Override
        public void setFocused(boolean focused) {
            setSearchOpen(focused);
        }

        @Override
        public boolean isFocused() {
            return searchOpen;
        }

        @Override
        public SelectionType getType() {
            return SelectionType.FOCUSED;
        }

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {}
    }
}
