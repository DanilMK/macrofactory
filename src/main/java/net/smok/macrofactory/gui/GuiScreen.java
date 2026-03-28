package net.smok.macrofactory.gui;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.config.gui.ButtonPressDirtyListenerSimple;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.interfaces.IConfigInfoProvider;
import fi.dy.masa.malilib.gui.interfaces.IDialogHandler;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;
import fi.dy.masa.malilib.render.GuiContext;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.KeyCodes;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.smok.macrofactory.MacroFactory;
import net.smok.macrofactory.ModulesKeybindProvider;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class GuiScreen<T, W extends GuiEntry<T>> extends GuiListBase<T, W, GuiList<T, W>> implements IKeybindConfigGui {

    protected final List<Runnable> hotkeyChangeListeners = new ArrayList<>();
    protected final ButtonPressDirtyListenerSimple dirtyListener = new ButtonPressDirtyListenerSimple();
    protected ConfigButtonKeybind activeKeybindButton;
    @Nullable protected IConfigInfoProvider hoverInfoProvider;
    @Nullable protected IDialogHandler dialogHandler;

    protected GuiScreen(int listX, int listY, String titleKey, Object... args) {
        super(listX, listY);
        this.title = StringUtils.translate(titleKey, args);
    }

    @Override
    protected void drawScreenBackground(GuiContext ctx, int mouseX, int mouseY) {
        super.drawScreenBackground(ctx, mouseX, mouseY);
    }


    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client != null && this.client.world == null) {
            this.renderPanoramaBackground(context, delta);
        }

        this.applyBlur(context);
        this.renderDarkening(context);
    }

    // Copy code because I can't extend GuiConfigBase.class
    @Override
    protected int getBrowserWidth()
    {
        return this.width - 20;
    }

    @Override
    protected int getBrowserHeight()
    {
        return this.height - 80;
    }

    @Nullable
    @Override
    public IDialogHandler getDialogHandler()
    {
        return this.dialogHandler;
    }


    @Override
    @Nullable
    public IConfigInfoProvider getHoverInfoProvider()
    {
        return this.hoverInfoProvider;
    }

    // Apply settings
    @Override
    protected void closeGui(boolean showParent) {
        super.closeGui(showParent);


        onSettingsChanged();
        ModulesKeybindProvider.update();
    }

    protected void onSettingsChanged()
    {
        ConfigManager.getInstance().onConfigsChanged(getModId());

        if (!this.hotkeyChangeListeners.isEmpty())
            InputEventHandler.getKeybindManager().updateUsedKeys();
    }

    @Override
    public boolean onKeyTyped(KeyInput input) {
        if (this.activeKeybindButton != null)
        {
            this.activeKeybindButton.onKeyPressed(input.key());
            return true;
        }
        else
        {
            //noinspection DataFlowIssue
            if (this.getListWidget().onKeyTyped(input))
            {
                return true;
            }

            if (input.key() == KeyCodes.KEY_ESCAPE && this.getParent() != GuiUtils.getCurrentScreen())
            {
                this.closeGui(true);
                return true;
            }

            return false;
        }
    }

    @Override
    public boolean onCharTyped(CharInput input) {
        if (this.activeKeybindButton != null)
        {
            // Prevents the chars leaking into the search box, if we didn't pretend to handle them here
            return true;
        }

        //noinspection DataFlowIssue
        if (this.getListWidget().onCharTyped(input))
        {
            return true;
        }

        return super.onCharTyped(input);
    }

    @Override
    public boolean onMouseClicked(Click click, boolean doubleClick) {
        if (super.onMouseClicked(click, doubleClick))
        {
            return true;
        }

        // When clicking on not-a-button, clear the selection
        if (this.activeKeybindButton != null)
        {
            this.activeKeybindButton.onClearSelection();
            this.setActiveKeybindButton(null);
            return true;
        }

        return false;
    }


    @Override
    public String getModId() {
        return MacroFactory.MOD_ID;
    }

    @Override
    public void clearOptions()
    {
        this.setActiveKeybindButton(null);
        this.hotkeyChangeListeners.clear();
    }

    /**
     * Unused.
     * @return always null.
     */

    @Override
    public List<GuiConfigsBase.ConfigOptionWrapper> getConfigs() {
        return null;
    }

    @Override
    public void addKeybindChangeListener(Runnable listener)
    {
        this.hotkeyChangeListeners.add(listener);
    }

    @Override
    public ButtonPressDirtyListenerSimple getButtonPressListener()
    {
        return this.dirtyListener;
    }

    @Override
    public void setActiveKeybindButton(@Nullable ConfigButtonKeybind button)
    {
        if (this.activeKeybindButton != null)
        {
            this.activeKeybindButton.onClearSelection();
            this.updateKeybindButtons();
        }

        this.activeKeybindButton = button;

        if (this.activeKeybindButton != null)
        {
            this.activeKeybindButton.onSelected();
        }
    }

    protected void updateKeybindButtons()
    {
        for (Runnable listener : this.hotkeyChangeListeners)
        {
            listener.run();
        }
    }

}
