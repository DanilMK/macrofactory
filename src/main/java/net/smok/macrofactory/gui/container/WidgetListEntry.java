package net.smok.macrofactory.gui.container;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.ScreenRect;
import net.smok.macrofactory.guiold.FilteredEntry;
import net.smok.macrofactory.gui.base.WidgetBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WidgetListEntry<T extends FilteredEntry> extends WidgetBase implements ParentContainer {

    protected final T entry;
    protected final WidgetList<T> parent;
    private final ArrayList<Element> elements = new ArrayList<>();
    private final ArrayList<Drawable> drawables = new ArrayList<>();

    private boolean dragging;
    private Element focused;

    public WidgetListEntry(int x, int y, int w, int h, T entry, WidgetList<T> parent) {
        super(x, y, w, h);
        this.entry = entry;
        this.parent = parent;
    }

    @Override
    public @NotNull ParentElement getParent() {
        return parent;
    }

    @Override
    public List<Element> children() {
        return elements;
    }

    @Override
    public List<? extends Drawable> drawables() {
        return drawables;
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return super.getNavigationFocus();
    }

    @Override
    public <C extends Drawable> Drawable addDrawable(@NotNull C drawableChild) {
        drawables.add(drawableChild);
        return drawableChild;
    }

    @Override
    public <C extends Element> Element addElement(@NotNull C elementChild) {
        elements.add(elementChild);
        return elementChild;
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
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

    public T getEntry() {
        return entry;
    }

}
