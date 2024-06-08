package net.smok.macrofactory.gui.base;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;

import java.util.function.Consumer;

public abstract class WidgetBase implements Widget {
    private int x, y, w, h;

    public WidgetBase(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public WidgetBase(int w, int h) {
        this.w = w;
        this.h = h;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getWidth() {
        return w;
    }

    public void setWidth(int w) {
        this.w = w;
    }

    @Override
    public int getHeight() {
        return h;
    }

    public void setHeight(int h) {
        this.h = h;
    }

    public int getMaxX() {
        return x + w;
    }

    public int getMaxY() {
        return y + h;
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {}

    public boolean contains(int x, int y) {
        return this.x < x && this.x + w > x && this.y < y && this.y + h > y;
    }
    public boolean contains(double mouseX, double mouseY) {
        return mouseX >= (double)this.getX() && mouseY >= (double)this.getY() && mouseX < (double)(this.getX() + this.w) && mouseY < (double)(this.getY() + this.h);
    }
}
