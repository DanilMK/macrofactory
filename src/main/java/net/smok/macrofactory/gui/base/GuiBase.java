package net.smok.macrofactory.gui.base;

import fi.dy.masa.malilib.util.KeyCodes;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;


public abstract class GuiBase extends Screen implements DialogHandler {

    private DialogWindow dialog;
    private Element defaultInputElement;


    protected GuiBase(Text title) {
        super(title);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isFocused()) {
            //noinspection DataFlowIssue
            if (getFocused().mouseClicked(mouseX, mouseY, button)) return true;
            else setFocused(null);
        }

        if (dialog != null) {
            if (dialog.contains(mouseX, mouseY)) dialog.mouseClicked(mouseX, mouseY, button);
            else closeDialog();

            return true;
        }

        for (Element child : children()) {
            if (child.mouseClicked(mouseX, mouseY, button)) {
                setFocused(child);
                if (button == 0) setDragging(true);
                break;
            }
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isFocused()) {
            //noinspection DataFlowIssue
            if (!getFocused().mouseReleased(mouseX, mouseY, button)) focusOn(null);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !isFocused() && dialog == null;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == KeyCodes.KEY_ESCAPE) {
            if (dialog != null) {
                closeDialog();
                return true;
            }

            if (isFocused()) {
                //noinspection DataFlowIssue
                getFocused().keyPressed(keyCode, scanCode, modifiers);
                setFocused(null);
                return true;
            }
        }

        if (!isFocused() && defaultInputElement != null && defaultInputElement.keyPressed(keyCode, scanCode, modifiers)) return true;

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (!isFocused() && defaultInputElement != null && defaultInputElement.charTyped(chr, modifiers)) setFocused(defaultInputElement);
        return super.charTyped(chr, modifiers);
    }

    @Override
    public void openDialog(DialogWindow dialog) {
        if (dialog == null || this.dialog != null) return;
        this.dialog = dialog;
        dialog.open();
    }

    @Override
    public void closeDialog() {
        if (dialog == null) return;
        dialog.close();
        dialog = null;
        setFocused(null);
    }


    @Override
    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    protected Element getDefaultInputElement() {
        return defaultInputElement;
    }

    protected void setDefaultInputElement(Element defaultInputElement) {
        this.defaultInputElement = defaultInputElement;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (dialog != null) {
            super.render(context, mouseX, mouseY, delta);
            context.getMatrices().push();
            context.getMatrices().translate(0, 0, 200);
            dialog.render(context, mouseX, mouseY, delta);
        }
        else super.render(context, mouseX, mouseY, delta);
    }
}
