package net.smok.macrofactory;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IConfigValue;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.StringUtils;
import net.smok.macrofactory.gui.ConfigsGui;
import net.smok.macrofactory.gui.modules.ModulesGui;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.Module;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Configs implements IConfigHandler, IKeybindProvider {

    public static final Configs INSTANCE = new Configs();
    private static final String CONFIG_FILE_NAME = MacroFactory.MOD_ID + ".json";
    private static final String MACRO_DIR = MacroFactory.MOD_ID + "_macros";

    @Override
    public void addKeysToMap(IKeybindManager manager) {
        manager.addKeybindToMap(Generic.MENU_OPEN.getKeybind());
        manager.addKeybindToMap(Generic.CMD_MACRO_OPEN.getKeybind());
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        manager.addHotkeysForCategory(MacroFactory.MOD_ID, "autoMacro.test", ImmutableList.of(
                Generic.MENU_OPEN, Generic.CMD_MACRO_OPEN
        ));
    }

    public static class Generic {
        public static final ConfigHotkey MENU_OPEN = new HotKeyWithCallBack("menuOpen", "", "Menu Open", (action, key) -> {
            GuiBase.openGui(new ConfigsGui(null));
            return true;
        });

        public static final ConfigHotkey CMD_MACRO_OPEN = new HotKeyWithCallBack("cmdMacroOpen", "Y", "Menu Open", (action, key) -> {
            GuiBase.openGui(new ModulesGui(null));
            return true;
        });


        public static final ImmutableList<IConfigValue> OPTIONS = ImmutableList.of(
                MENU_OPEN, CMD_MACRO_OPEN
        );

    }

    public static class Macros {

        public static List<Module> Modules;

        static {
            Module creative = new Module("module.default.default");

            creative.add(new Macro(creative, StringUtils.translate("macro.default.sample_command"), "KP_7", "/say Hello World!"));
            creative.add(new Macro(creative, StringUtils.translate("macro.default.auto_use"), "KP_8", true, 0, PlayerKeybind.USE));
            creative.add(new Macro(creative, StringUtils.translate("macro.default.auto_attack"), "KP_9", true, 20, PlayerKeybind.ATTACK));

            Modules = new ArrayList<>();
            Modules.add(creative);
        }
    }



    @Override
    public void load() {
        readGeneric();
        readMacros();
    }

    private static void readGeneric() {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (!configFile.exists() || !configFile.isFile() || !configFile.canRead()) return;
        JsonElement element = JsonUtils.parseJsonFile(configFile);

        if (element == null || !element.isJsonObject()) return;
        JsonObject root = element.getAsJsonObject();

        try
        {
            ConfigUtils.readConfigBase(root, "Generic", Generic.OPTIONS);

        } catch (Exception ignored) {
            MacroFactory.LOGGER.info("Corrupted Options file. ");
        }
    }

    private static void readMacros() {
        File macrosDir = new File(FileUtils.getConfigDirectory(), MACRO_DIR);
        if (!macrosDir.exists() || !macrosDir.isDirectory()) return;

        File[] files = macrosDir.listFiles(pathname -> pathname.getName().endsWith(".json"));
        if (files == null) return;
        List<Module> modules = new ArrayList<>();

        for (File moduleFile : files) {
            if (!moduleFile.exists() || !moduleFile.isFile() || !moduleFile.canRead()) continue;
            try {
                JsonElement element = JsonUtils.parseJsonFile(moduleFile);
                if (element == null || !element.isJsonObject()) continue;

                Module module = new Module();
                module.setValueFromJsonElement(element);


                modules.add(module);

            } catch (Exception e) {
                MacroFactory.LOGGER.info("Corrupted Options file. \n"+e.getLocalizedMessage());
            }

        }
        Macros.Modules = modules;
    }

    @Override
    public void save() {
        File dir = FileUtils.getConfigDirectory();

        writeGeneric(dir);

        writeMacros(dir);

    }

    private static void writeGeneric(File dir) {
        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs())
        {
            JsonObject genericJson = new JsonObject();
            ConfigUtils.writeConfigBase(genericJson, "Generic", Generic.OPTIONS);
            JsonUtils.writeJsonToFile(genericJson, new File(dir, CONFIG_FILE_NAME));
        }
    }

    private static void writeMacros(File dir) {
        File macrosDir = new File(dir, MACRO_DIR);
        if ((macrosDir.exists() && macrosDir.isDirectory()) || macrosDir.mkdirs()) {

            File[] files = macrosDir.listFiles(pathname -> pathname.getName().endsWith(".json"));
            if (files != null) for (File file : files) //noinspection ResultOfMethodCallIgnored
                file.delete();

            for (Module module : Macros.Modules) {
                JsonUtils.writeJsonToFile(module.getAsJsonElement(), new File(macrosDir, module.getName() + ".json"));
            }
        }
    }
}
