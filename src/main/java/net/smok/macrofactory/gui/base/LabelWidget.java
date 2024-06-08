package net.smok.macrofactory.gui.base;

import fi.dy.masa.malilib.gui.LeftRight;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class LabelWidget extends WidgetBase implements Drawable {
    private final Text labelText;
    private final LeftRight align;
    private final TextRenderer textRenderer;
    private final int color;

    public LabelWidget(int x, int y, int w, int h, String labelText) {
        this(x, y, w, h, labelText, 0xFFFFFFFF);
    }
    public LabelWidget(int x, int y, int w, int h, String labelText, int color) {
        this(x, y, w, h, labelText, LeftRight.LEFT, color);
    }

    public LabelWidget(int x, int y, int w, int h, String labelText, LeftRight align, int color) {
        super(x, y, w, h);
        this.align = align;
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.color = color;
        this.labelText = trimText(w, labelText);
    }

    @NotNull
    private Text trimText(int w, String labelString) {

        labelString = StringUtils.translate(labelString);
        if (StringUtils.getStringWidth(labelString) <= w) return Text.of(labelString);

        for (int i = 1; i < labelString.length(); i++) {

            if (StringUtils.getStringWidth(labelString.substring(0, i)) < w) continue;
            labelString = labelString.substring(0, i - 1);
        }
        return Text.of(labelString);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (align == LeftRight.CENTER) context.drawCenteredTextWithShadow(textRenderer, labelText, (getX() + getWidth() / 2), posY(), color);
        else if (align == LeftRight.RIGHT) context.drawTextWithShadow(textRenderer, labelText, getX() + getWidth() - StringUtils.getStringWidth(labelText.getString()), posY(), color);
        else context.drawTextWithShadow(textRenderer, labelText, getX(), posY(), color);
    }

    private int posY() {
        return getY() + getHeight() / 2 - 4;
    }

}
