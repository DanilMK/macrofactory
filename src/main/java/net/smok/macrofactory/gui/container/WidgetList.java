package net.smok.macrofactory.gui.container;

import fi.dy.masa.malilib.gui.LeftRight;
import fi.dy.masa.malilib.gui.MaLiLibIcons;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.widgets.WidgetSearchBar;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import net.smok.macrofactory.MacroFactory;
import net.smok.macrofactory.guiold.FilteredEntry;
import net.smok.macrofactory.gui.base.BoxRender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class WidgetList<T extends FilteredEntry> extends ScrollableWidget implements ParentContainer {


    private final List<WidgetListEntry<T>> widgetEntries = new ArrayList<>();
    private final ParentElement parent;
    private int space = 5;
    private int nextPosition;
    private WidgetSearch widgetSearchBar;
    private int browserEntriesOffsetY;
    private BoxRender box;

    private Element focused;
    private boolean dragging;

    public WidgetList(int x, int y, int w, int h, ParentElement parent) {
        super(x, y, w, h, Text.empty());
        this.parent = parent;

        refreshPositions();
        /*
        widgetEntries.clear();
        nextPosition = getEntryStartY();
        MacroFactory.LOGGER.info("Refresh entries");

        if (widgetSearchBar == null || !widgetSearchBar.hasFilter()) addNonFilteredContents(this::createWidgetEntry);
        else addFilteredContents(this::createWidgetEntry);*/
    }

    @Override
    public @NotNull ParentElement getParent() {
        return parent;
    }

    @Override
    public List<Element> children() {
        ArrayList<Element> result = new ArrayList<>(widgetEntries);
        if (widgetSearchBar != null) result.add(widgetSearchBar);
        return result;
    }

    @Override
    public List<Drawable> drawables() {
        ArrayList<Drawable> result = new ArrayList<>(widgetEntries);
        if (widgetSearchBar != null) result.add(widgetSearchBar);
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


    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!this.visible) return;

        if (widgetSearchBar != null) widgetSearchBar.render(context, mouseX, mouseY, delta);
        if (box != null) box.drawBox(context, getEntryStartX(), getEntryStartY(), getEntryWidth(), getHeight() - browserEntriesOffsetY);

        context.enableScissor(getEntryStartX(), getEntryStartY(), getEntryStartX() + getEntryWidth(), getEntryStartY() + getHeight());

        if (mouseY < getY() || mouseY > getY() + getHeight()) mouseY = -1;
        else mouseY += (int) getScrollY();

        context.getMatrices().push();
        context.getMatrices().translate(0.0, -this.getScrollY(), 0.0);
        this.renderContents(context, mouseX, mouseY, delta);
        context.getMatrices().pop();
        context.disableScissor();
        this.renderOverlay(context);
    }

    @Override
    protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
        for (WidgetListEntry<T> entry : widgetEntries) {
            entry.render(context, mouseX, mouseY, delta);
        }
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean clicked = super.mouseClicked(mouseX, mouseY, button);
        if (!clicked) return false;

        if (isWithinBounds(mouseX, mouseY)) ParentContainer.super.mouseClicked(mouseX, mouseY + getScrollY(), button);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        return ParentContainer.super.mouseReleased(mouseX, mouseY + getScrollY(), button);
    }
    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (ParentContainer.super.keyPressed(keyCode, scanCode, modifiers)) {
            if (getFocused() instanceof Widget widget) {

                setScrollY((double) (widget.getY() - getY()) * getMaxScrollY() / getContentsHeight());
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    // Public get/set


    public BoxRender getBox() {
        return box;
    }

    public void setBox(BoxRender box) {
        this.box = box;
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
        return getX();
    }

    protected int getEntryStartY() {
        return getY() + browserEntriesOffsetY;
    }

    protected static int getEntryHeight() {
        return 20;
    }

    protected int getEntryWidth() {
        return getWidth() - getPadding();
    }

    @Override
    protected int getContentsHeight() {
        return nextPosition;
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 9;
    }




    // Creating content
    public void refreshEntries() {
        widgetEntries.clear();
        nextPosition = getEntryStartY();
        MacroFactory.LOGGER.info("Refresh entries");

        if (widgetSearchBar == null || !widgetSearchBar.hasFilter()) addNonFilteredContents(this::createWidgetEntry);
        else addFilteredContents(this::createWidgetEntry);
    }

    public void refreshPositions() {
        Map<T, WidgetListEntry<T>> oldEntries = widgetEntries.stream().collect(Collectors.toMap(WidgetListEntry::getEntry, entry -> entry));
        widgetEntries.clear();
        nextPosition = getEntryStartY();
        MacroFactory.LOGGER.info("Refresh positions");

        if (widgetSearchBar == null || !widgetSearchBar.hasFilter()) addNonFilteredContents(t -> refreshWidgetEntry(t, oldEntries));
        else addFilteredContents(t -> refreshWidgetEntry(t, oldEntries));

    }

    private void refreshWidgetEntry(T t, Map<T, WidgetListEntry<T>> oldEntries) {
        if (oldEntries.containsKey(t)) {
            WidgetListEntry<T> entry = oldEntries.get(t);
            oldEntries.remove(t);
            if (entry == null) return;
            entry.setY(nextPosition);
            widgetEntries.add(entry);
            nextPosition += entry.getHeight() + space;
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
        WidgetListEntry<T> widgetEntry = createWidgetEntry(entry, getEntryStartX(), nextPosition, getEntryWidth(), getEntryHeight());
        if (widgetEntry == null) return;
        widgetEntries.add(widgetEntry);
        nextPosition += widgetEntry.getHeight() + space;
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
