package net.smok.macrofactory.gui.config;

import fi.dy.masa.malilib.config.ConfigType;
import fi.dy.masa.malilib.config.IConfigValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.smok.macrofactory.gui.base.TextFieldBase;
import org.jetbrains.annotations.NotNull;

public class ConfigStringWidget extends TextFieldBase {

    public ConfigStringWidget(int width, int height, @NotNull IConfigValue config, int maxLength) {
        this(0, 0, width, height, config, maxLength);
    }
    public ConfigStringWidget(int x, int y, int width, int height, @NotNull IConfigValue config, int maxLength) {
        super(MinecraftClient.getInstance().textRenderer, x, y, width, height, Text.of(config.getName()));
        textField.setMaxLength(maxLength);
        textField.setText(config.getStringValue());
        textField.setChangedListener(value -> {
            if (value.isEmpty() && config.getType() == ConfigType.INTEGER || config.getType() == ConfigType.DOUBLE) config.setValueFromString("0");
            else config.setValueFromString(value);
        });

        if (config.getType() == ConfigType.INTEGER) textField.setTextPredicate(this::integerPredicate);
        else if (config.getType() == ConfigType.DOUBLE) textField.setTextPredicate(this::doublePredicate);


        if (config.getComment() != null)
            textField.setTooltip(Tooltip.of(Text.translatable(config.getComment())));

    }

}
