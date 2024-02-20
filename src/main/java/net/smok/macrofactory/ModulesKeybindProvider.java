package net.smok.macrofactory;

import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import net.smok.macrofactory.macros.Module;

public class ModulesKeybindProvider implements IKeybindProvider {

    public static final ModulesKeybindProvider INSTANCE = new ModulesKeybindProvider();

    private IKeybindManager manager;

    public static void update() {
        INSTANCE.manager.updateUsedKeys();
    }

    @Override
    public void addKeysToMap(IKeybindManager manager) {
        this.manager = manager;
        for (Module collection : Configs.Macros.Modules) {
            collection.addKeysToMap(manager);
        }
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {

        for (Module collection : Configs.Macros.Modules) {
            collection.addHotkeys(manager);
        }
    }

}
