package net.smok.macrofactory.gui;

import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import org.jetbrains.annotations.NotNull;

public record PositionAlignment(boolean attachLeft, int size, int shift) {

    public PositionAlignment(boolean attachLeft, int size, int shift) {
        this.shift = shift;
        this.size = size;
        this.attachLeft = attachLeft;
    }
    public PositionAlignment(boolean attachLeft, int x, @NotNull IGuiIcon icon) {
        this(attachLeft, icon.getWidth(), x);
    }

    public PositionAlignment(boolean attachLeft, int width) {
        this(attachLeft, width, 0);
    }
    public PositionAlignment(boolean attachLeft) {
        this(attachLeft, Integer.MAX_VALUE, 0);
    }
    public PositionAlignment(boolean attachLeft, @NotNull IGuiIcon icon) {
        this(attachLeft, icon.getWidth(), 0);
    }


    public int getSize(int maxWidth) {
        return size < 0 || maxWidth < 0 ? 0 : Math.min(size, maxWidth);
    }
    public boolean isLeft() {
        return attachLeft;
    }
    public int getShift() {
        return shift;
    }

    public int getX(int minEmptyX, int maxEmptyX) {
        if (attachLeft) return minEmptyX + shift;
        return maxEmptyX - getSize(maxEmptyX - minEmptyX) - shift;
    }
}
