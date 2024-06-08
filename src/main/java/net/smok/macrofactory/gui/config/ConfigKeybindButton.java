package net.smok.macrofactory.gui.config;

import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeybindCategory;
import fi.dy.masa.malilib.util.KeyCodes;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.smok.macrofactory.gui.base.FocusLock;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ConfigKeybindButton extends ButtonWidget implements FocusLock {

    private final IKeybind keybind;
    protected boolean firstKey;
    protected final List<String> overlapInfo = new ArrayList<>();
    private final String comment;
    public ConfigKeybindButton(int width, int height, IHotkey hotkey) {
        super(0, 0, width, height, Text.of(hotkey.getName()), null, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
        this.keybind = hotkey.getKeybind();
        this.comment = hotkey.getComment() != null ? hotkey.getComment() : "";
        this.updateDisplayString();
        this.updateTooltip();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isFocused()) {
            if (keyCode == KeyCodes.KEY_ESCAPE) {
                if (firstKey) keybind.clearKeys();
            }
            else addKey(keyCode);

            updateDisplayString();
        }
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!clicked(mouseX, mouseY)) return false;
        if (isFocused()) {
            addKey(button - 100);
            updateDisplayString();

            return true;
        }

        if (button == 1) keybind.resetToDefault();
        //else parent.setFocused(this);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return isMouseOver(mouseX, mouseY);
    }

    private void addKey(int keyCode) {
        if (firstKey) {
            keybind.clearKeys();
            firstKey = false;
        }

        keybind.addKey(keyCode);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (focused) this.firstKey = true;

        updateDisplayString();
        updateTooltip();
    }




    public void updateDisplayString()
    {
        String valueStr = this.keybind.getKeysDisplayString();

        if (this.keybind.getKeys().isEmpty() || StringUtils.isBlank(valueStr)) {
            valueStr = "NONE";
        }

        if (this.isFocused()) {
            setMessage(Text.of("> " + GuiBase.TXT_YELLOW + valueStr + GuiBase.TXT_RST + " <"));
        } else {

            this.updateConflicts();

            if (!this.overlapInfo.isEmpty()) {
                setMessage(Text.of(GuiBase.TXT_GOLD + valueStr + GuiBase.TXT_RST));
            } else {
                setMessage(Text.of(valueStr));
            }
        }
    }

    protected void updateTooltip() {
        if (comment.isEmpty() && overlapInfo.isEmpty()) {
            setTooltip(null);
            return;
        }

        MutableText text = Text.translatable(comment);
        if (!this.overlapInfo.isEmpty()) {
            for (String s : overlapInfo) text.append(Text.of("\n" + s));
        }

        setTooltip(Tooltip.of(text));
    }

    protected void updateConflicts() {

        List<KeybindCategory> categories = InputEventHandler.getKeybindManager().getKeybindCategories();
        List<IHotkey> overlaps = new ArrayList<>();
        this.overlapInfo.clear();

        for (KeybindCategory category : categories) {
            List<? extends IHotkey> hotkeys = category.getHotkeys();

            for (IHotkey hotkey : hotkeys) if (this.keybind.overlaps(hotkey.getKeybind())) overlaps.add(hotkey);

            if (!overlaps.isEmpty()) {
                if (!this.overlapInfo.isEmpty()) {
                    this.overlapInfo.add("-----");
                }

                this.overlapInfo.add(category.getModName());
                this.overlapInfo.add(" > " + category.getCategory());

                for (IHotkey overlap : overlaps) {
                    String key = " [ " + GuiBase.TXT_GOLD + overlap.getKeybind().getKeysDisplayString() + GuiBase.TXT_RST + " ]";
                    this.overlapInfo.add("    - " + overlap.getName() + key);
                }

                overlaps.clear();
            }
        }

    }
}
