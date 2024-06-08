package net.smok.macrofactory.gui.config;

import fi.dy.masa.malilib.config.IConfigBoolean;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.smok.macrofactory.gui.base.ButtonBase;
import net.smok.macrofactory.gui.base.GuiIcon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigBooleanButton extends ButtonBase {

    private final IConfigBoolean config;
    @Nullable
    private GuiIcon tIcon, fIcon;

    public ConfigBooleanButton(@NotNull GuiIcon tIcon, @NotNull GuiIcon fIcon, @NotNull IConfigBoolean config, @Nullable PressAction onPress) {
        this(tIcon.getWidth(), tIcon.getHeight(), config, onPress);
        this.tIcon = tIcon;
        this.fIcon = fIcon;
        refreshIcon();

    }
    public ConfigBooleanButton(int x, int y, @NotNull GuiIcon tIcon, @NotNull GuiIcon fIcon, @NotNull IConfigBoolean config, @Nullable PressAction onPress) {
        this(x, y, tIcon.getWidth(), tIcon.getHeight(), config, onPress);
        this.tIcon = tIcon;
        this.fIcon = fIcon;
        refreshIcon();
    }

    public ConfigBooleanButton(int width, int height, @NotNull IConfigBoolean config, @Nullable PressAction onPress) {
        this(0, 0, width, height, config, onPress);
    }


    public ConfigBooleanButton(int x, int y, int width, int height, @NotNull IConfigBoolean config, @Nullable PressAction onPress) {
        super(x, y, width, height, Text.of(config.getStringValue()), onPress);
        this.config = config;

        if (config.getComment() != null)
            setTooltip(Tooltip.of(Text.translatable(config.getComment())));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!clicked(mouseX, mouseY)) return false;

        playDownSound(MinecraftClient.getInstance().getSoundManager());
        if (button == 0) config.setBooleanValue(!config.getBooleanValue());
        else if (button == 1) config.resetToDefault();
        refreshIcon();
        setMessage(Text.of(config.getStringValue()));
        onPress();

        return true;
    }

    private void refreshIcon() {
        setIcon(config.getBooleanValue() ? tIcon : fIcon);
    }

}
