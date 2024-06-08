package net.smok.macrofactory.gui.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigOptionList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.smok.macrofactory.gui.base.ButtonBase;
import org.jetbrains.annotations.NotNull;

public class ConfigOptionListButton extends ButtonBase {

    private final IConfigOptionList config;


    public ConfigOptionListButton(int width, int height, @NotNull IConfigOptionList config) {
        this(width, height, config, null);
    }
    public ConfigOptionListButton(int width, int height, @NotNull IConfigOptionList config, PressAction onPress) {
        super(width, height, Text.of(config.getOptionListValue().getDisplayName()), onPress);
        this.config = config;

        if (config instanceof IConfigBase configBase && configBase.getComment() != null)
            setTooltip(Tooltip.of(Text.translatable(configBase.getComment())));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!clicked(mouseX, mouseY)) return false;

        playDownSound(MinecraftClient.getInstance().getSoundManager());
        config.setOptionListValue(config.getOptionListValue().cycle(button == 0));
        setMessage(Text.of(config.getOptionListValue().getDisplayName()));
        if (onPress != null) onPress.onPress(this);

        return true;
    }

}
