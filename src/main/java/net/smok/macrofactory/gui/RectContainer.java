package net.smok.macrofactory.gui;

import org.jetbrains.annotations.NotNull;

public class RectContainer {
    protected final int spaceX, spaceY, minX, maxX, y;
    private final int lineHeight;
    private int minEmptyX, maxEmptyX, line, height;

    public RectContainer(int spaceX, int spaceY, int minX, int maxX, int y, int lineHeight) {
        this.spaceX = spaceX;
        this.spaceY = spaceY;
        this.minX = minX;
        this.maxX = maxX;
        this.y = y;
        this.lineHeight = lineHeight;
        minEmptyX = minX;
        maxEmptyX = maxX;
        height = lineHeight;
    }
    protected void addLine() {
        line++;
        height = (lineHeight + spaceY) * (line + 1);
        minEmptyX = minX;
        maxEmptyX = maxX;
    }
    protected int getLineY() {
        return y + (lineHeight + spaceY) * line;
    }

    protected int maxWidth() {
        return maxEmptyX - minEmptyX;
    }

    public Rect align(@NotNull PositionAlignment alignment) {
        int width = alignment.getSize(maxWidth());
        int x;

        if (alignment.isLeft()) {
            x = minEmptyX + alignment.shift();
            minEmptyX = x + width + spaceX;
        } else {
            x = maxEmptyX - width - alignment.shift();
            maxEmptyX = x - spaceX;
        }


        return new Rect(x, getLineY(), width, lineHeight);
    }


    public int getHeight() {
        return height;
    }
}
