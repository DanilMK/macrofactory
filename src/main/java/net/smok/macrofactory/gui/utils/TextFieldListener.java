package net.smok.macrofactory.gui.utils;

import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.interfaces.ITextFieldListener;

public class TextFieldListener implements ITextFieldListener<GuiTextFieldGeneric> {
    private final IConfigValue bindValue;
    private final boolean allowEmptyField;

    public TextFieldListener(IConfigValue bindValue, boolean allowEmptyField) {
        this.bindValue = bindValue;
        this.allowEmptyField = allowEmptyField;
    }


    @Override
    public boolean onGuiClosed(GuiTextFieldGeneric textField) {
        if (allowEmptyField || !textField.getValue().isEmpty())
            bindValue.setValueFromString(textField.getValue());
        return ITextFieldListener.super.onGuiClosed(textField);
    }

    @Override
    public boolean onTextChange(GuiTextFieldGeneric textField) {
        if (allowEmptyField || !textField.getValue().isEmpty())
            bindValue.setValueFromString(textField.getValue());
        return false;
    }
}
