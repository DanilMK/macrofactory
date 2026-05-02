package net.smok.macrofactory.macros.actions;

import fi.dy.masa.malilib.config.options.ConfigOptionList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.smok.macrofactory.PlayerKeybind;
import net.smok.macrofactory.TickLoop;
import net.smok.macrofactory.macros.Macro;
import org.jetbrains.annotations.NotNull;

public final class PlayerAction extends ConfigOptionList implements MacroAction {


    public PlayerAction(String name, String comment) {
        super(name, PlayerKeybind.USE, comment);
    }

    private static void attack(Minecraft client) {

        LocalPlayer player = client.player;
        if (player == null || player.isSpectator()) return;

        HitResult hit = client.hitResult;
        if (hit instanceof EntityHitResult entityHit && client.gameMode != null) {
            client.gameMode.attack(player, entityHit.getEntity());
        }
    }


    @Override
    public void start(@NotNull Minecraft client, Macro macro) {
        if (getKeyBinding().clicked()) {
            TickLoop.removeFromLoop(macro);
            return;
        }

        getKeyBinding().down(client);
        if (macro.getCd() > 3 && getKeyBinding() == PlayerKeybind.ATTACK) attack(client);
    }

    @Override
    public void end(@NotNull Minecraft client, Macro macro) {
        if (getKeyBinding().isDown()) getKeyBinding().up(client);
    }

    public PlayerKeybind getKeyBinding() {
        return (PlayerKeybind) getOptionListValue();
    }


}
