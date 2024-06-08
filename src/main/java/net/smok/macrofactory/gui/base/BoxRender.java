package net.smok.macrofactory.gui.base;

import net.minecraft.client.gui.DrawContext;

public class BoxRender {

    public static final int COLOR_BORDER_ALPHA = 0xAAA0A0A0;
    public static final int COLOR_BACK_ALPHA = 0xAA000000;
    public static final int COLOR_BORDER = 0xFFA0A0A0;
    public static final int COLOR_BACK = 0xFF000000;


    public static BoxRender BLACK_BOX = new BoxRender(COLOR_BORDER, COLOR_BACK);
    public static BoxRender BLACK_BOX_ALPHA = new BoxRender(COLOR_BORDER_ALPHA, COLOR_BACK_ALPHA);

    private final int borderColor;
    private final int backColor;

    public BoxRender(int borderColor, int backColor) {
        this.borderColor = borderColor;
        this.backColor = backColor;
    }


    public void drawBox(DrawContext context, int x, int y, int width, int height) {
        context.drawBorder(x, y, width, height, borderColor);
        context.fill(x, y, x + width, y + height, backColor);
    }
}
