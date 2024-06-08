package net.smok.macrofactory.gui;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import net.smok.macrofactory.HotKeyWithCallBack;
import net.smok.macrofactory.MacroFactory;
import net.smok.macrofactory.gui.base.*;
import net.smok.macrofactory.gui.container.FixedContainer;
import net.smok.macrofactory.gui.config.ConfigKeybindButton;
import net.smok.macrofactory.gui.config.ConfigStringWidget;

public class DialogSample extends DialogWindow {

    private static ConfigBoolean configOptionList = new ConfigBoolean("Sample", false, "Sample comment");
    private static ConfigDouble configString = new ConfigDouble("Sample", 12, "Default sample comment");
    private static ConfigHotkey hotkey = new HotKeyWithCallBack("Sample", "", "Hotkey sample", (action, key) -> {
        MacroFactory.LOGGER.info("Key presed");
        return true;
    });

    public DialogSample(int x, int y, int w, int h, DialogHandler parent) {
        super(x, y, w, h, parent);

        addDrawable(new BoxWidget(x, y, w, h, BoxWidget.COLOR_BORDER_ALPHA, BoxWidget.COLOR_BACK_ALPHA));
        FixedContainer container = new FixedContainer(x, y, w, h, 5, true, this);


        //container.add(new ConfigBooleanButton(100, 15, configOptionList));
        container.add(new ConfigStringWidget( 100, 15, configString, 100));
        container.add(new ConfigKeybindButton(100, 15, hotkey));


        addDrawableElement(container);
    }


}
