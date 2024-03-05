package net.smok.macrofactory.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigOptionList;
import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.button.*;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;
import fi.dy.masa.malilib.gui.widgets.*;
import fi.dy.masa.malilib.gui.wrappers.TextFieldWrapper;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.render.RenderUtils;
import fi.dy.masa.malilib.util.KeyCodes;
import net.minecraft.client.gui.DrawContext;
import net.smok.macrofactory.gui.utils.TextFieldListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class GuiEntry<T> extends WidgetConfigOptionBase<T> {

    protected final IKeybindConfigGui host;
    private final ArrayList<TextFieldWrapper<? extends GuiTextFieldGeneric>> textFields = new ArrayList<>();

    private final RectContainer rectContainer;


    public GuiEntry(int x, int y, int width, int lineHeight, int space, WidgetListConfigOptionsBase<?, ?> parent,
                    T entry, int listIndex, IKeybindConfigGui host) {
        super(x, y, width, lineHeight, parent, entry, listIndex);
        this.host = host;
        rectContainer = new RectContainer(space, 2, x, x + width, y, lineHeight - 2);
    }

    public abstract void init();





    protected GuiTextFieldGeneric addTextField(PositionAlignment alignment, IConfigValue config) {
        return addTextField(alignment, config, config.getComment(), 256);
    }
    protected GuiTextFieldGeneric addTextField(PositionAlignment alignment, IConfigValue config, int maxTextfieldTextLength) {
        return addTextField(alignment, config, config.getComment(), maxTextfieldTextLength);
    }
    protected GuiTextFieldGeneric addTextField(PositionAlignment alignment, IConfigValue config, @Nullable String comment, int maxTextfieldTextLength)
    {
        Rect rect = addRect(alignment);
        GuiTextFieldGeneric field = new GuiTextFieldGeneric(rect.x() + 2, rect.y() + 2, rect.width() - 4, rect.height() - 4, textRenderer);
        field.setMaxLength(maxTextfieldTextLength);
        field.setText(config.getStringValue());


        TextFieldWrapper<? extends GuiTextFieldGeneric> wrapper = new TextFieldWrapper<>(field, new TextFieldListener(config));
        textFields.add(wrapper);
        parent.addTextField(wrapper);

        if (comment != null && !comment.isEmpty()) {
            addComment(rect.x(), rect.y(), rect.width(), rect.height(), comment);
        }
        return field;
    }

    protected void addOptionListButton(PositionAlignment alignment, IConfigOptionList config, IButtonActionListener pressListener) {
        addOptionListButton(alignment, config, pressListener, ((IConfigBase)config).getComment());
    }

    protected void addOptionListButton(PositionAlignment alignment, IConfigOptionList config, IButtonActionListener pressListener, @Nullable String comment) {
        Rect rect = addRect(alignment);
        ConfigButtonOptionList optionButton = new ConfigButtonOptionList(rect.x(), rect.y(), rect.width(), rect.height(), config);


        addButton(optionButton, pressListener);
        addCommentForWidget(optionButton, comment);
    }

    protected void addKeybindButton(PositionAlignment alignment, IHotkey hotkey) {
        addKeybindButton(alignment, hotkey, hotkey.getComment());
    }
    protected void addKeybindButton(PositionAlignment alignment, IHotkey hotkey, @Nullable String comment) {
        Rect rect = addRect(alignment);
        ConfigButtonKeybind keybindButton = new ConfigButtonKeybind(rect.x(), rect.y(), rect.width(), rect.height(),
                hotkey.getKeybind(), this.host);


        addButton(keybindButton, this.host.getButtonPressListener());
        addCommentForWidget(keybindButton, comment);
    }

    protected ButtonGeneric addGenericButton(boolean attachLeft, IGuiIcon icon, IButtonActionListener listener, @Nullable String comment) {
        return addGenericButton(new PositionAlignment(attachLeft, icon), icon, listener, comment);
    }
    protected ButtonGeneric addSwitchButton(boolean attachLeft, IGuiIcon icon, boolean on, IButtonActionListener listener, @Nullable String comment) {
        return addSwitchButton(new PositionAlignment(attachLeft, icon), icon, on, listener, comment);
    }
    protected ButtonGeneric addGenericButton(PositionAlignment alignment, IGuiIcon icon, IButtonActionListener listener, @Nullable String comment) {
        Rect rect = addRect(alignment);
        ButtonGeneric button = addButton(new ButtonGeneric(rect.x(), rect.y(), icon), listener);

        addCommentForWidget(button, comment);
        return button;
    }
    protected ButtonGeneric addSwitchButton(PositionAlignment alignment, IGuiIcon icon, boolean on, IButtonActionListener listener, @Nullable String comment) {
        Rect rect = addRect(alignment);
        ButtonGeneric button = addButton(new ButtonSwitch(rect.x(), rect.y(), icon, on), listener);

        addCommentForWidget(button, comment);
        return button;
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

    protected void addLabel(PositionAlignment alignment, int textColor, String... lines) {
        Rect rect = addRect(alignment);
        addLabel(rect.x(), rect.y(), rect.width(), rect.height(), textColor, lines);
    }



    // Position and space control

    public Rect addRect(PositionAlignment alignment) {
        return rectContainer.addRect(alignment);
    }

    protected void addLine() {
        rectContainer.addLine();
        height = rectContainer.getHeight();
    }

    protected int maxWidth() {
        return rectContainer.maxWidth();
    }

    protected int space() {
        return rectContainer.spaceX;
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
            if (!textField.getTextField().getText().equals(this.lastAppliedValue))
                return true;
        }

        return false;
    }
    @Override
    public void applyNewValueToConfig() {
        for (TextFieldWrapper<?> textField : textFields) {
            textField.onGuiClosed();
            lastAppliedValue = textField.getTextField().getText();
        }
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton)
    {
        boolean ret = false;

        // Focus to any TextField
        for (TextFieldWrapper<?> textField : textFields)
            ret |= textField.getTextField().mouseClicked(mouseX, mouseY, mouseButton);

        // Click to any sub widget
        if (!this.subWidgets.isEmpty())
            for (WidgetBase widget : this.subWidgets)
                ret |= widget.isMouseOver(mouseX, mouseY) && widget.onMouseClicked(mouseX, mouseY, mouseButton);

        return ret;
    }

    @Override
    public boolean onKeyTypedImpl(int keyCode, int scanCode, int modifiers)
    {
        for (TextFieldWrapper<?> textField : textFields) {
            if (!textField.isFocused()) continue;

            if (keyCode == KeyCodes.KEY_ENTER) {

                // Apply value for each focused field
                textField.onGuiClosed();
                lastAppliedValue = textField.getTextField().getText();
                return true;

            } else {
                // KeyType for each focused field
                return textField.onKeyTyped(keyCode, scanCode, modifiers);
            }
        }

        return false;
    }

    @Override
    protected boolean onCharTypedImpl(char charIn, int modifiers)
    {
        // CharType for each field
        for (TextFieldWrapper<?> textField : textFields)
            if (textField.onCharTyped(charIn, modifiers)) return true;

        return super.onCharTypedImpl(charIn, modifiers);
    }

    @Override
    public void render(int mouseX, int mouseY, boolean selected, DrawContext drawContext)
    {
        RenderUtils.color(1f, 1f, 1f, 1f);

        for (TextFieldWrapper<?> textField : textFields)
            textField.getTextField().render(drawContext, mouseX, mouseY, 0f);

        super.render(mouseX, mouseY, selected, drawContext);
    }
}
