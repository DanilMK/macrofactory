package net.smok.macrofactory.macros;

import com.google.gson.*;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemIcon extends ConfigBase<ItemIcon> implements IConfigBase {

    private final Item defaultItemStack;
    private Item itemStack;



    public ItemIcon(String name, ItemStack defaultItemStack, String comment) {
        super(null, name, comment);
        this.defaultItemStack = defaultItemStack.getItem();
        itemStack = defaultItemStack.getItem();
    }

    public ItemStack getItemStack() {
        return itemStack == null ? ItemStack.EMPTY : itemStack.getDefaultInstance();
    }

    public void setItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            this.itemStack = null;
        }
        else {
            this.itemStack = itemStack.getItem();

        }
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        JsonObject json = element.getAsJsonObject();
        if (json.has("Type") && json.get("Type").isJsonPrimitive())
            itemStack = BuiltInRegistries.ITEM.getValue(Identifier.parse(json.get("Type").getAsString()));
    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonObject json = new JsonObject();
        json.addProperty("Type", itemStack == null ? "" : itemStack.toString());
        return json;
    }

    @Override
    public boolean isModified() {
        return itemStack != defaultItemStack;
    }

    @Override
    public void resetToDefault() {
        itemStack = defaultItemStack;
    }

    public void setIconFromHand(int mouseButton) {
        if (mouseButton != 0) return;
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;
        setItemStack(client.player.getInventory().getSelectedItem());

    }

}
