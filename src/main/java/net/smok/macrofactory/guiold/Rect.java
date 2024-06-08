package net.smok.macrofactory.guiold;

public record Rect(int x, int y, int width, int height) {

    public boolean contains(int x, int y) {
        return this.x < x && this.x + width > x && this.y < y && this.y + height > y;
    }
}
