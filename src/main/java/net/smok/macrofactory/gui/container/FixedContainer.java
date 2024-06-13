package net.smok.macrofactory.gui.container;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.smok.macrofactory.gui.base.WidgetBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FixedContainer extends WidgetBase implements ParentContainer {


    private final ArrayList<Widget> widgets = new ArrayList<>();
    private final ArrayList<Element> elements = new ArrayList<>();
    private final ArrayList<Drawable> drawables = new ArrayList<>();
    private final ParentElement parent;
    private final int space;
    private final boolean isVertical;
    private final boolean reverse;
    private int nextPosition;

    private boolean dragging;
    private Element focused;

    public FixedContainer(int x, int y, int w, int h, ParentElement parent, int space, boolean isVertical, boolean reverse) {
        super(x, y, w, h);
        this.parent = parent;
        this.space = space;
        this.isVertical = isVertical;
        this.reverse = reverse;
    }
    public FixedContainer(int x, int y, int w, int h, int space, boolean isVertical, ParentElement parent) {
        this(x, y, w, h, parent, space, isVertical, false);
    }





    public int nextX() {
        return getX() + (isVertical ? 0 :
                reverse ? getWidth() - nextPosition : nextPosition);
    }

    public int nextY() {
        return getY() + (!isVertical ? 0 :
                reverse ? getHeight() - nextPosition : nextPosition);
    }

    public int contentWidth() {
        return isVertical ? getWidth() : nextPosition - space;
    }

    public int contentHeight() {
        return isVertical ? nextPosition - space : getHeight();
    }

    public int emptySpace() {
        return isVertical ? getHeight() - nextPosition : getWidth() - nextPosition;
    }

    public <T extends Widget> T add(T widget) {
        if (widget instanceof Drawable drawable) addDrawable(drawable);
        if (widget instanceof Element element) addElement(element);


        return addWidget(widget);
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

    public List<Widget> widgets() {
        return widgets;
    }

    @Override
    public <T extends Drawable> Drawable addDrawable(@NotNull T drawableChild) {
        drawables.add(drawableChild);
        return drawableChild;
    }

    @Override
    public <T extends Element> Element addElement(@NotNull T elementChild) {
        elements.add(elementChild);
        return elementChild;
    }

    public <T extends Widget> T addWidget(T widget) {
        widgets.add(widget);
        if (reverse) widget.setPosition(nextX() - (isVertical ? 0 : widget.getWidth()), nextY() - (isVertical ? widget.getHeight() : 0));
        else widget.setPosition(nextX(), nextY());

        if (isVertical) nextPosition += widget.getHeight() + space;
        else nextPosition += widget.getWidth() + space;

        return widget;
    }

    public void clear() {
        widgets.clear();
        drawables.clear();
        elements.clear();
        nextPosition = 0;
    }

    public int getSpace() {
        return space;
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

    @Override
    public ScreenRect getNavigationFocus() {
        return super.getNavigationFocus();
    }

    @Override
    public @Nullable GuiNavigation getNavigation(int keyCode) {
        return switch (keyCode) {
            case 258 -> new GuiNavigation.Tab(!Screen.hasShiftDown());
            case 262 -> isVertical ? null : new GuiNavigation.Arrow(NavigationDirection.RIGHT);
            case 263 -> isVertical ? null : new GuiNavigation.Arrow(NavigationDirection.LEFT);
            case 264 -> isVertical ? new GuiNavigation.Arrow(NavigationDirection.DOWN) : null;
            case 265 -> isVertical ? new GuiNavigation.Arrow(NavigationDirection.UP) : null;
            default -> null;
        };
    }
}
