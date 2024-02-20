package net.smok.macrofactory;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MacroFactory implements ModInitializer {
	public static final String MOD_ID = "macrofactory";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		LOGGER.info("Initialize Macro Factory");
		ClientLifecycleEvents.CLIENT_STARTED.register(this::clientStart);
		ClientTickEvents.END_CLIENT_TICK.register(new TickLoop());
		InputEventHandler.getKeybindManager().registerKeybindProvider(ModulesKeybindProvider.INSTANCE);

		InputEventHandler.getKeybindManager().registerKeybindProvider(Configs.INSTANCE);
		ConfigManager.getInstance().registerConfigHandler(MOD_ID, Configs.INSTANCE);
	}

	private void clientStart(MinecraftClient client) {
		PlayerKeybind.init(client);
	}
}