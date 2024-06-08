package net.smok.macrofactory.gui.base;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.ParentElement;

public interface DialogHandler extends ParentElement {

    void openDialog(DialogWindow dialog);

    void closeDialog();

    TextRenderer getTextRenderer();

}
