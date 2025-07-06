package net.smok.macrofactory.macros.actions;

import fi.dy.masa.malilib.config.options.ConfigOptionList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.smok.macrofactory.PlayerKeybind;
import net.smok.macrofactory.TickLoop;
import net.smok.macrofactory.macros.Macro;
import org.jetbrains.annotations.NotNull;

public final class PlayerAction extends ConfigOptionList implements MacroAction {

    public PlayerAction() {
        super("Keybind", PlayerKeybind.USE, "");
    }

    public PlayerAction(PlayerKeybind keyBinding) {
        super("Keybind", PlayerKeybind.USE, "");
        setOptionListValue(keyBinding);
    }

    public PlayerAction(String name, String comment) {
        super(name, PlayerKeybind.USE, comment);
    }

    private static void attack(MinecraftClient client) {

        ClientPlayerEntity player = client.player;
        if (player == null || player.isSpectator() || !player.canHit()) return;

        HitResult hit = client.crosshairTarget;
        if (hit instanceof EntityHitResult entityHit && client.interactionManager != null) {
            client.interactionManager.attackEntity(player, entityHit.getEntity());
        }
    }


    @Override
    public void run(@NotNull MinecraftClient client, Loop loop, Macro macro) {
        if (loop != Loop.END && getKeyBinding().wasPressed(client)) {
            TickLoop.removeFromLoop(macro);
            return;
        }

        switch (loop) {

            case TICK -> {
                getKeyBinding().setPressed(client, true);
                if (macro.getCd() > 3 && getKeyBinding() == PlayerKeybind.ATTACK) attack(client);
            }
            case END, OFF_TICK -> getKeyBinding().setPressed(client, false);
        }
    }


    public PlayerKeybind getKeyBinding() {
        return (PlayerKeybind) getOptionListValue();
    }


}
