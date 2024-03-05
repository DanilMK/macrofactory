package net.smok.macrofactory.macros;

import com.google.gson.*;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ItemIcon extends ConfigBase<ItemIcon> implements IConfigBase {

    private ItemStack itemStack;
    private final ItemStack defaultItemStack;

    public ItemIcon(String name, ItemStack defaultItemStack, String comment) {
        super(null, name, comment);
        this.defaultItemStack = defaultItemStack;
        itemStack = defaultItemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) this.itemStack = ItemStack.EMPTY;
        else this.itemStack = itemStack.copy();
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        JsonObject json = element.getAsJsonObject();
        Item type = Registries.ITEM.get(new Identifier(json.get("Type").getAsString()));
        itemStack = type.getDefaultStack();
        itemStack.setDamage(json.get("Damage").getAsInt());

        // todo add nbt compound

    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("Type", itemStack.getItem().toString());
        json.addProperty("Damage", itemStack.getDamage());

        if (!itemStack.hasNbt()) return json;

        // todo add nbt compound
        return json;
    }

    @Override
    public boolean isModified() {
        return !ItemStack.areItemsEqual(defaultItemStack, itemStack);
    }

    @Override
    public void resetToDefault() {
        itemStack = defaultItemStack;
    }

    public void setIconFromHand(ButtonBase buttonBase, int mouseButton) {
        if (mouseButton != 0) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        setItemStack(client.player.getInventory().getMainHandStack());

    }

    public void drawIcon(DrawContext drawContext, int x, int y, int width, int height) {
        drawContext.drawItem(getItemStack(), x + width / 2 - 8, y + height / 2 - 8);
    }
}
