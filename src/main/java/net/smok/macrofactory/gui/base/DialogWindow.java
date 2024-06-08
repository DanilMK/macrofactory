package net.smok.macrofactory.gui.base;

import net.minecraft.client.gui.*;
import net.smok.macrofactory.gui.container.ParentContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DialogWindow extends WidgetBase implements ParentContainer, Drawable {
    private final List<Element> children = new ArrayList<>();
    private final List<Drawable> drawables = new ArrayList<>();
    private final DialogHandler parent;

    private boolean dragging;
    private Element focused;

    public DialogWindow(int x, int y, int w, int h, DialogHandler parent) {
        super(x, y, w, h);
        this.parent = parent;
    }

    public void open() {}
    public void close() {}

    @Override
    public ScreenRect getNavigationFocus() {
        return new ScreenRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public <T extends Element> Element addElement(@NotNull T elementChild) {
        children.add(elementChild);
        return elementChild;
    }

    @Override
    public <T extends Drawable> Drawable addDrawable(@NotNull T drawableChild) {
        drawables.add(drawableChild);
        return drawableChild;
    }

    @Override
    public List<Element> children() {
        return children;
    }

    @Override
    public List<? extends Drawable> drawables() {
        return drawables;
    }

    @Override
    public @NotNull ParentElement getParent() {
        return parent;
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
}
