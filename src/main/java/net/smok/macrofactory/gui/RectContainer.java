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
    public void addLine() {
        line++;
        height = (lineHeight + spaceY) * (line + 1);
        minEmptyX = minX;
        maxEmptyX = maxX;
    }
    public int getLineY() {
        return y + (lineHeight + spaceY) * line;
    }
    public int getMinY() {
        return y;
    }

    public int maxWidth() {
        return maxEmptyX - minEmptyX;
    }



    public Rect addRect(@NotNull PositionAlignment alignment) {
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

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.minX && mouseX < maxX &&
                mouseY >= this.y && mouseY < this.y + this.height;
    }


    public int getHeight() {
        return height;
    }
}
