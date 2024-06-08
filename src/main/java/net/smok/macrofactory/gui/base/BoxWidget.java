package net.smok.macrofactory.gui.base;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;

public class BoxWidget extends WidgetBase implements Drawable {

    public static final int COLOR_BORDER_ALPHA = 0xAAA0A0A0;
    public static final int COLOR_BACK_ALPHA = 0xAA000000;
    public static final int COLOR_BORDER = 0xFFA0A0A0;
    public static final int COLOR_BACK = 0xFF000000;

    private final int borderColor;
    private final int backColor;



    public BoxWidget(int x, int y, int w, int h) {
        this(x, y, w, h, COLOR_BORDER, COLOR_BACK);
    }
    public BoxWidget(int x, int y, int w, int h, int borderColor, int backColor) {
        super(x, y, w, h);
        this.borderColor = borderColor;
        this.backColor = backColor;
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawBorder(getX(), getY(), getWidth(), getHeight(), borderColor);
        context.fill(getX(), getY(), getMaxX(), getMaxY(), backColor);
    }
}
