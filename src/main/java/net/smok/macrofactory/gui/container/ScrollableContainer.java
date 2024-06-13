package net.smok.macrofactory.gui.container;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import net.smok.macrofactory.gui.base.BoxRender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScrollableContainer extends ScrollableWidget implements ParentContainer {

    private final FixedContainer container;
    private BoxRender box;



    public ScrollableContainer(int x, int y, int w, int h, Text text, int padding, int space, ParentElement parent) {
        super(x, y, w, h, text);
        container = new FixedContainer(x + padding, y + padding, w - padding * 2, h - padding * 2, space, true, parent);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean clicked = super.mouseClicked(mouseX, mouseY, button);
        if (!clicked) return false;

        if (isWithinBounds(mouseX, mouseY)) container.mouseClicked(mouseX, mouseY + getScrollY(), button);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        return container.mouseReleased(mouseX, mouseY + getScrollY(), button);
    }

    public BoxRender getBox() {
        return box;
    }

    public void setBox(BoxRender box) {
        this.box = box;
    }

    @Override
    public boolean isDragging() {
        return container.isDragging();
    }

    @Override
    public void setDragging(boolean dragging) {
        container.setDragging(dragging);
    }

    @Nullable
    @Override
    public Element getFocused() {
        return container.getFocused();
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        container.setFocused(focused);
    }

    @Override
    protected void drawBox(DrawContext context, int x, int y, int width, int height) {
        if (box != null) box.drawBox(context, x, y, width, height);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (container.keyPressed(keyCode, scanCode, modifiers)) {
            if (container.getFocused() instanceof Widget widget) {

                setScrollY((double) (widget.getY() - getY()) * getMaxScrollY() / getContentsHeight());
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected int getContentsHeight() {
        return container.contentHeight();
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 9;
    }

    @Override
    protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
        if (mouseY < getY() || mouseY > getY() + getHeight()) mouseY = -1;
        else mouseY += (int) getScrollY();
        container.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        container.appendNarrations(builder);
    }

    @Override
    public @NotNull ParentElement getParent() {
        return container.getParent();
    }

    @Override
    public List<? extends Element> children() {
        return container.children();
    }

    @Override
    public List<? extends Drawable> drawables() {
        return container.drawables();
    }

    @Override
    public <C extends Element> Element addElement(@NotNull C elementChild) {
        return container.addElement(elementChild);
    }

    @Override
    public <C extends Drawable> Drawable addDrawable(@NotNull C drawableChild) {
        return container.addDrawable(drawableChild);
    }

    public <T extends Widget> Widget add(T widget) {
        return container.add(widget);
    }

    public void clear() {
        container.clear();
    }

    public FixedContainer getContainer() {
        return container;
    }
}
