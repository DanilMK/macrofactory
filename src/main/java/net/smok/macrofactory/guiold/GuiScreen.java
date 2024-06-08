package net.smok.macrofactory.guiold;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.config.gui.ButtonPressDirtyListenerSimple;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiListBase;
import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.interfaces.IConfigInfoProvider;
import fi.dy.masa.malilib.gui.interfaces.IDialogHandler;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;
import fi.dy.masa.malilib.util.GuiUtils;
import fi.dy.masa.malilib.util.KeyCodes;
import fi.dy.masa.malilib.util.StringUtils;
import net.smok.macrofactory.MacroFactory;
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

    public void setDialogHandler(@Nullable IDialogHandler handler)
    {
        this.dialogHandler = handler;
    }

    @Override
    @Nullable
    public IConfigInfoProvider getHoverInfoProvider()
    {
        return this.hoverInfoProvider;
    }

    @Override
    public void removed()
    {/*
        if (this.getListWidget().wereConfigsModified())
        {
            this.getListWidget().applyPendingModifications();
            this.onSettingsChanged();
            this.getListWidget().clearConfigsModifiedFlag();
        }*/
    }

    protected void onSettingsChanged()
    {
        ConfigManager.getInstance().onConfigsChanged(getModId());

        if (!this.hotkeyChangeListeners.isEmpty())
            InputEventHandler.getKeybindManager().updateUsedKeys();
    }

    @Override
    public boolean onKeyTyped(int keyCode, int scanCode, int modifiers)
    {
        if (this.activeKeybindButton != null)
        {
            this.activeKeybindButton.onKeyPressed(keyCode);
            return true;
        }
        else
        {
            if (this.getListWidget().onKeyTyped(keyCode, scanCode, modifiers))
            {
                return true;
            }

            if (keyCode == KeyCodes.KEY_ESCAPE && this.getParent() != GuiUtils.getCurrentScreen())
            {
                this.closeGui(true);
                return true;
            }

            return false;
        }
    }

    @Override
    public boolean onCharTyped(char charIn, int modifiers)
    {
        if (this.activeKeybindButton != null)
        {
            // Prevents the chars leaking into the search box, if we didn't pretend to handle them here
            return true;
        }

        if (this.getListWidget().onCharTyped(charIn, modifiers))
        {
            return true;
        }

        return super.onCharTyped(charIn, modifiers);
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (super.onMouseClicked(mouseX, mouseY, mouseButton))
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
