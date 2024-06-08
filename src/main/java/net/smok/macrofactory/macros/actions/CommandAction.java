package net.smok.macrofactory.macros.actions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.smok.macrofactory.guiold.ButtonSwitch;
import net.smok.macrofactory.macros.Macro;
import net.smok.macrofactory.macros.SmokUtils;
import org.jetbrains.annotations.NotNull;

public class CommandAction implements MacroAction {

    private final ConfigBoolean inChat;
    private final ConfigString command;

    private final String name;

    public CommandAction(String name, String inChatComment, String commandComment) {
        this.name = name;
        inChat = new ConfigBoolean("In Chat", false, inChatComment);
        command = new ConfigString("Command", "Hello world!", commandComment);
    }


    public void setValueFromJsonElement(JsonElement element) {
        JsonObject object = element.getAsJsonObject();
        SmokUtils.setValueFromJsonElement(object, inChat);
        SmokUtils.setValueFromJsonElement(object, command);
    }

    public JsonElement getAsJsonElement() {
        JsonObject object = new JsonObject();
        SmokUtils.getAsJsonElement(object, inChat);
        SmokUtils.getAsJsonElement(object, command);
        return object;
    }

    public String getName() {
        return name;
    }

    public ConfigBoolean getInChat() {
        return inChat;
    }
    public void switchInChat(ButtonBase button, int mouseButton) {
        if (mouseButton != 0) return;

        inChat.setBooleanValue(!inChat.getBooleanValue());
        if (button instanceof ButtonSwitch buttonSwitch) buttonSwitch.setOn(inChat.getBooleanValue());
    }

    public ConfigString getCommand() {
        return command;
    }

    @Override
    public void run(@NotNull MinecraftClient client, Loop loop, Macro macro) {
        ClientPlayerEntity player = client.player;
        if (player == null || loop == Loop.OFF_TICK || loop == Loop.END) return;

        if (inChat.getBooleanValue()) GuiBase.openGui(new ChatScreen(substringValue()));
        else sendMessageOrCommand(player, substringValue());
    }

    private String substringValue() {
        String value = command.getStringValue();
        return value.length() > 256 ? value.substring(0, 256) : value;
    }
    private void sendMessageOrCommand(ClientPlayerEntity player, String message) {
        if (message.startsWith("/")) player.networkHandler.sendChatCommand(message.substring(1));
        else player.networkHandler.sendChatMessage(message);
    }
}
