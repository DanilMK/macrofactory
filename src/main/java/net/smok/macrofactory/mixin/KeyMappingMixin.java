package net.smok.macrofactory.mixin;

import net.minecraft.client.KeyMapping;
import net.smok.macrofactory.PlayerKeybind;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {

    @Inject(
            method = "lambda$click$0",
            at = @At("HEAD")
    )
    private static void onKeyClick(KeyMapping keyMapping, CallbackInfo ci) {
        PlayerKeybind.click(keyMapping);
    }
}
