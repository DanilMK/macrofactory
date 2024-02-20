package net.smok.macrofactory;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.smok.macrofactory.macros.Macro;

import java.util.ArrayList;
import java.util.List;

public class TickLoop implements ClientTickEvents.EndTick {

    private static final List<Macro> macrosInLoop = new ArrayList<>();
    private static final List<Macro> remover = new ArrayList<>();


    private static boolean breaking;

    public static void removeFromLoop(Macro macro) {
        if (!remover.contains(macro)) remover.add(macro);
    }

    @Override
    public void onEndTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        if (client.currentScreen instanceof ChatScreen) return;

        if (client.currentScreen != null || breaking) {
            breaking = false;
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
    public static void breakLoop() {
        breaking = true;
    }
    private static void breakLoop(MinecraftClient client) {
        for (Macro macro : macrosInLoop) macro.endLoop(client);
        macrosInLoop.clear();
    }

}
