package net.smok.macrofactory.gui.base;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public abstract class TextFieldBase extends ClickableWidget implements Drawable, Selectable, Element, FocusLock {
    protected static final Pattern PATTER_INTEGER = Pattern.compile("-?[0-9]*");
    protected static final Pattern PATTER_DOUBLE = Pattern.compile("^-?([0-9]+(\\.[0-9]*)?)?");
    protected final TextFieldWidget textField;
    protected TextListener textListener;

    public TextFieldBase(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
        super(x, y, width, height, text);
        textField = new TextFieldWidget(textRenderer, x + 2, y + 2, width - 4, height - 4, text);
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        textField.setX(x + 2);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        textField.setY(y + 2);
    }

    public TextListener getTextListener() {
        return textListener;
    }

    public void setTextListener(TextListener textListener) {
        this.textListener = textListener;
    }

    protected boolean doublePredicate(String string) {
        if (string == null) return false;
        if (string.isEmpty()) return true;
        return PATTER_DOUBLE.matcher(string).matches();
    }

    protected boolean integerPredicate(String string) {
        if (string == null) return false;
        if (string.isEmpty()) return true;
        return PATTER_INTEGER.matcher(string).matches();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {

            if (button == 1) textField.setText("");
            else if (button != 0) return false;

            textField.onClick(mouseX, mouseY);
            //parent.setFocused(this);
            return true;
        }
        return false;
    }

    private boolean isHovered(double mouseX, double mouseY) {
        return textField.active && textField.visible && isMouseOver(mouseX, mouseY);
    }

    @Override
    public void setFocused(boolean focused) {
        textField.setFocused(focused);
        if (!focused && textListener != null) textListener.onTextChange(textField.getText());
    }

    @Override
    public boolean isFocused() {
        return textField.isFocused();
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawBorder(getX(), getY(), getWidth(), getHeight(), isMouseOver(mouseX, mouseY) ? 0xFF0A0A0A : 0xFF000000);
        textField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return isMouseOver(mouseX, mouseY);
    }

    @Override
    public SelectionType getType() {
        return textField.getType();
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        textField.appendClickableNarrations(builder);
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return super.getNavigationFocus();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return textField.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return textField.charTyped(chr, modifiers);
    }

    public void setEditable(boolean editable) {
        textField.setEditable(editable);
    }

    public void setSuggestion(@Nullable String suggestion) {
        textField.setSuggestion(suggestion);
    }

    @Environment(EnvType.CLIENT)
    public interface TextListener {
        void onTextChange(String text);
    }

}
