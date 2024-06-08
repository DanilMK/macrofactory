package net.smok.macrofactory.gui.base;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ButtonBase extends ButtonWidget {

    @Nullable
    private GuiIcon icon;

    public ButtonBase(@NotNull GuiIcon icon, PressAction onPress) {
        this(0, 0, icon, onPress);
    }

    public ButtonBase(int x, int y, @NotNull GuiIcon icon, PressAction onPress) {
        super(x, y, icon.getWidth(), icon.getHeight(), Text.empty(), onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.icon = icon;
    }
    public ButtonBase(int width, int height, Text message, PressAction onPress) {
        this(0, 0, width, height, message, onPress);
    }
    public ButtonBase(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    }

    public @Nullable GuiIcon getIcon() {
        return icon;
    }

    public void setIcon(@Nullable GuiIcon icon) {
        this.icon = icon;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        return false;
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        if (icon != null) icon.renderAt(context, getX(), getY(), active, isSelected());
        else super.renderButton(context, mouseX, mouseY, delta);
    }

    @Override
    public void onPress() {
        if (onPress != null) onPress.onPress(this);
    }
}
