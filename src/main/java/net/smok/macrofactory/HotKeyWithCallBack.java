package net.smok.macrofactory;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;

public class HotKeyWithCallBack extends ConfigHotkey {
    public HotKeyWithCallBack(String name, String defaultStorageString, String comment, IHotkeyCallback callBack) {
        super(name, defaultStorageString, comment);
        getKeybind().setCallback(callBack);
    }

    public HotKeyWithCallBack(String name, String defaultStorageString, KeybindSettings settings, String comment, IHotkeyCallback callback) {
        super(name, defaultStorageString, settings, comment);
        getKeybind().setCallback(callback);
    }
}
