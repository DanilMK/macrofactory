package net.smok.macrofactory.gui.utils;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.interfaces.ITextFieldListener;

public class EmptyTextListener implements ITextFieldListener<GuiTextFieldGeneric> {

    @Override
    public boolean onTextChange(GuiTextFieldGeneric textField) {
        return false;
    }
}
