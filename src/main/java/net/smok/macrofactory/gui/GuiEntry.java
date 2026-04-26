package net.smok.macrofactory.gui;

import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;
import fi.dy.masa.malilib.gui.widgets.*;
import fi.dy.masa.malilib.gui.wrappers.TextFieldWrapper;
import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.util.KeyCodes;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.smok.macrofactory.gui.utils.TextFieldListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class GuiEntry<T> extends WidgetConfigOptionBase<T> {

    public static final int MIN_BUTTON_SIZE = 60;
    public static final int MAX_BUTTON_SIZE = 200;


    protected final IKeybindConfigGui host;
    protected final int bSize;
    protected final int space;
    private final ArrayList<TextFieldWrapper<? extends GuiTextFieldGeneric>> textFields = new ArrayList<>();




    public GuiEntry(int x, int y, int width, int lineHeight, int space, WidgetListConfigOptionsBase<?, ?> parent,
                    T entry, int listIndex, IKeybindConfigGui host) {
        super(x, y, width, lineHeight, parent, entry, listIndex);
        this.host = host;
        this.space = space;
        bSize = 20 + space;
    }

    public abstract void init();


    protected GuiTextFieldGeneric addTextField(int x, int y, int width, int height, IConfigValue config, @Nullable String comment, int maxTextfieldTextLength, boolean allowEmptyField) {

        GuiTextFieldGeneric field = new GuiTextFieldGeneric(x + 2, y, width - 4, height - 2, textRenderer);


        TextFieldWrapper<? extends GuiTextFieldGeneric> wrapper = new TextFieldWrapper<>(field, new TextFieldListener(config, allowEmptyField));
        textFields.add(wrapper);
        parent.addTextField(wrapper);
        field.setMaxLength(maxTextfieldTextLength);
        field.insertText(config.getStringValue());

        if (comment != null && !comment.isEmpty()) {
            addComment(x, y, width, height, comment);
        }
        return field;
    }

    // Common Widgets

    protected void addCommentForWidget(WidgetBase widget, @Nullable String comment) {
        if (comment != null && !comment.isEmpty())
            addComment(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), comment);
    }

    protected void addComment(int x, int y, int width, int height, @NotNull String comment)
    {
        addWidget(new WidgetHoverInfo(x, y, width, height, comment));
    }


    protected int space() {
        return space;
    }

    // Override methods

    @Override
    public boolean wasConfigModified() {
        return false;
    }

    public boolean hasPendingModifications()
    {
        // Check if any TextField don't is not last applied value
        for (TextFieldWrapper<?> textField : textFields) {
            if (!textField.textField().getValue().equals(this.lastAppliedValue))
                return true;
        }

        return false;
    }
    @Override
    public void applyNewValueToConfig() {
        for (TextFieldWrapper<?> textField : textFields) {
            textField.onGuiClosed();
            lastAppliedValue = textField.textField().getValue();
        }
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        if (super.isMouseOver(mouseX, mouseY)) return true;
        if (!subWidgets.isEmpty())
            for (WidgetBase widget : subWidgets)
                if (widget.isMouseOver(mouseX, mouseY)) return true;

        return false;
    }

    @Override
    protected boolean onMouseClickedImpl(MouseButtonEvent click, boolean doubleClick) {
        boolean ret = false;

        // Focus to any TextField
        for (TextFieldWrapper<?> textField : textFields)
            ret |= textField.textField().mouseClicked(click, doubleClick);

        // Click to any sub widget
        if (!this.subWidgets.isEmpty())
            for (WidgetBase widget : this.subWidgets)
                ret |= widget.isMouseOver((int) click.x(), (int) click.y()) && widget.onMouseClicked(click, doubleClick);

        return ret;
    }

    @Override
    public boolean onKeyTypedImpl(KeyEvent input) {
        for (TextFieldWrapper<?> textField : textFields) {
            if (!textField.isFocused()) continue;

            if (input.key() == KeyCodes.KEY_ENTER) {

                // Apply value for each focused field
                textField.onGuiClosed();
                lastAppliedValue = textField.textField().getValue();
                return true;

            } else {
                // KeyType for each focused field
                return textField.onKeyTyped(input);
            }
        }

        return false;
    }

    @Override
    protected boolean onCharTypedImpl(CharacterEvent input) {
        // CharType for each field
        for (TextFieldWrapper<?> textField : textFields)
            if (textField.onCharTyped(input)) return true;

        return super.onCharTypedImpl(input);
    }

    @Override
    public void render(GuiContext drawContext, int mouseX, int mouseY, boolean selected) {
        for (TextFieldWrapper<?> textField : textFields) {
            textField.textField().extractWidgetRenderState(drawContext, mouseX, mouseY, 0f);
        }
        super.render(drawContext, mouseX, mouseY, selected);
    }

}
