package net.smok.macrofactory;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.smok.macrofactory.macros.Macro;

import java.util.ArrayList;
import java.util.List;

public class TickLoop implements ClientTickEvents.EndTick {

    private static final List<Macro> macrosInLoop = new ArrayList<>();
    private static final List<Macro> remover = new ArrayList<>();


    public static void removeFromLoop(Macro macro) {
        if (!remover.contains(macro)) remover.add(macro);
    }

    @Override
    public void onEndTick(Minecraft client) {
        if (client.player == null || client.level == null) return;

        if (client.screen instanceof ChatScreen) return;

        if (client.screen != null) {
            breakLoop(client);
        }

        for (Macro macro : remover) {
            macro.endLoop(client);
            macrosInLoop.remove(macro);
        }
        remover.clear();

        for (Macro macro : macrosInLoop) macro.tickLoop(client);

    }
    
    public static void addToLoop(Macro macro) {
        macrosInLoop.add(macro);
    }

    private static void breakLoop(Minecraft client) {
        for (Macro macro : macrosInLoop) macro.endLoop(client);
        macrosInLoop.clear();
    }

}
