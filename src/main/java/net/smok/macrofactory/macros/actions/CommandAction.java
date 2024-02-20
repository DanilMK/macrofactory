package net.smok.macrofactory.macros.actions;

import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.gui.GuiBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.smok.macrofactory.macros.Macro;
import org.jetbrains.annotations.NotNull;

public class CommandAction extends ConfigString implements MacroAction {



    public CommandAction() {
        this("Hello world!");
    }
    public CommandAction(String command) {
        super("commandAction", command, "");
    }

    public CommandAction(String name, String comment) {
        super(name, "", comment);
    }

    @Override
    public void run(@NotNull MinecraftClient client, Loop loop, Macro macro) {
        ClientPlayerEntity player = client.player;
        if (player == null || loop == Loop.OFF_TICK || loop == Loop.END) return;

        if (macro.isChat()) GuiBase.openGui(new ChatScreen(substringValue()));
        else sendMessageOrCommand(player, substringValue());
    }

    private String substringValue() {
        String value = getStringValue();
        return value.length() > 256 ? value.substring(0, 256) : value;
    }
    private void sendMessageOrCommand(ClientPlayerEntity player, String message) {
        if (message.startsWith("/")) player.networkHandler.sendChatCommand(message.substring(1));
        else player.networkHandler.sendChatMessage(message);
    }
}
