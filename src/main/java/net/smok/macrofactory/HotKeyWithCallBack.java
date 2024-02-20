package net.smok.macrofactory;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;

public class HotKeyWithCallBack extends ConfigHotkey {
    public HotKeyWithCallBack(String name, String defaultStorageString, String comment, IHotkeyCallback callBack) {
        super(name, defaultStorageString, comment);
        getKeybind().setCallback(callBack);
    }
}
