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
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.data.json.JsonUtils;
import net.smok.macrofactory.gui.modules.ModulesGui;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.Module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Configs implements IConfigHandler, IKeybindProvider {

    public static final Configs INSTANCE = new Configs();
    private static final String CONFIG_FILE_NAME = MacroFactory.MOD_ID + ".json";
    private static final String MACRO_DIR = MacroFactory.MOD_ID + "_macros";
    private static final Path CONFIG_FILE = FileUtils.getConfigDirectory().resolve(CONFIG_FILE_NAME);
    private static final Path MACRO_DIRECTORY = FileUtils.getConfigDirectory().resolve(MACRO_DIR);


    @Override
    public void addKeysToMap(IKeybindManager manager) {
        manager.addKeybindToMap(Generic.CMD_MACRO_OPEN.getKeybind());
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        manager.addHotkeysForCategory(MacroFactory.MOD_ID, "autoMacro.test", ImmutableList.of(
                Generic.CMD_MACRO_OPEN
        ));
    }

    public static class Generic {

        public static final ConfigHotkey CMD_MACRO_OPEN = new HotKeyWithCallBack("modules_open", "Y", "config.comment.modules_open", (_, _) -> {
            GuiBase.openGui(new ModulesGui(null));
            return true;
        });


        public static final ImmutableList<IConfigValue> OPTIONS = ImmutableList.of(
                CMD_MACRO_OPEN
        );

    }

    public static class Macros {

        public static final List<Module> Modules;

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
        JsonElement element = JsonUtils.parseJsonFile(CONFIG_FILE);

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

        if (!Files.exists(MACRO_DIRECTORY)) return;

        try (var files = Files.list(MACRO_DIRECTORY)) {
            Macros.Modules.clear();
            files.filter(path -> path.toString().endsWith(".json"))
                    .map(file -> Module.readFromJson(JsonUtils.parseJsonFile(file)))
                    .filter(Objects::nonNull).forEach(Macros.Modules::add);
        } catch (IOException e) {
            MacroFactory.LOGGER.error("Error while reading macros.", e);
        }
    }

    @Override
    public void save() {
        writeGeneric();
        writeMacros();
    }

    private static void writeGeneric() {
        JsonObject genericJson = new JsonObject();
        ConfigUtils.writeConfigBase(genericJson, "Generic", Generic.OPTIONS);
        JsonUtils.writeJsonToFile(genericJson, CONFIG_FILE);
    }

    private static void writeMacros() {
        FileUtils.createDirectoriesIfMissing(MACRO_DIRECTORY);


        try (var files = Files.list(MACRO_DIRECTORY).filter(path -> path.toString().endsWith(".json"))) {
            files.forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    MacroFactory.LOGGER.error("Cannot delete macros file.", e);
                }
            });
        } catch (IOException e) {
            MacroFactory.LOGGER.error("Cannot delete macros files.", e);
        }

        Map<String, Integer> count = new HashMap<>();

        for (Module module : Macros.Modules) {
            if (module.markAsDeleted) continue;
            int n = 0;
            String moduleName = module.getName().isEmpty() ? "Unnamed module" : module.getName();
            if (count.containsKey(moduleName)) n = count.get(moduleName);

            count.put(moduleName, n + 1);
            String fileName = n == 0 ? moduleName + ".json" : moduleName + n + ".json";

            JsonUtils.writeJsonToFile(module.getAsJsonElement(), MACRO_DIRECTORY.resolve(fileName));
        }
    }
}
