package net.smok.macrofactory.gui.utils;

import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.interfaces.ITextFieldListener;

public class TextFieldListener implements ITextFieldListener<GuiTextFieldGeneric> {
    private final IConfigValue bindValue;

    public TextFieldListener(IConfigValue bindValue) {
        this.bindValue = bindValue;
    }


    @Override
    public boolean onGuiClosed(GuiTextFieldGeneric textField) {
        if (!textField.getValue().isEmpty())
            bindValue.setValueFromString(textField.getValue());
        return ITextFieldListener.super.onGuiClosed(textField);
    }

    @Override
    public boolean onTextChange(GuiTextFieldGeneric textField) {
        if (!textField.getValue().isEmpty())
            bindValue.setValueFromString(textField.getValue());
        return false;
    }
}
