/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import raffel080108.minecrafteras.MinecraftEras;

public final class UtilData {
    private static UtilData utilDataInstance;
    private NamespacedKey campMasterKey;
    private NamespacedKey guiItemKey;
    private NamespacedKey guiItemDataKey;
    private ItemStack previousPageItem;
    private ItemStack nextPageItem;
    
    private UtilData() {}

    public static UtilData getInstance() {
        if (utilDataInstance == null)
            utilDataInstance = new UtilData();

        return utilDataInstance;
    }


    public void initializeData() {
        MinecraftEras main = MinecraftEras.getInstance();
        campMasterKey = new NamespacedKey(main, "camp-master");
        guiItemKey = new NamespacedKey(main, "gui-item");
        guiItemDataKey = new NamespacedKey(main, "gui-item-data");

        ItemStack previousPageItem = new ItemStack(Material.ARROW);
        ItemMeta previousPageItemMeta = previousPageItem.getItemMeta();
        previousPageItemMeta.displayName(Component.text("Previous page").color(NamedTextColor.GREEN));
        previousPageItemMeta.getPersistentDataContainer().set(guiItemKey, PersistentDataType.INTEGER, 2);
        previousPageItem.setItemMeta(previousPageItemMeta);
        this.previousPageItem = previousPageItem;

        ItemStack nextPageItem = new ItemStack(Material.ARROW);
        ItemMeta nextPageItemMeta = nextPageItem.getItemMeta();
        nextPageItemMeta.displayName(Component.text("Next page").color(NamedTextColor.GREEN));
        nextPageItemMeta.getPersistentDataContainer().set(guiItemKey, PersistentDataType.INTEGER, 2);
        nextPageItem.setItemMeta(nextPageItemMeta);
        this.nextPageItem = nextPageItem;
    }

    public NamespacedKey getCampMasterKey() {
        return campMasterKey;
    }

    public NamespacedKey getGuiItemKey() {
        return guiItemKey;
    }

    public NamespacedKey getGuiItemDataKey() {
        return guiItemDataKey;
    }

    public ItemStack getPreviousPageItem(int page) {
        ItemStack item = previousPageItem.clone();
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(guiItemDataKey, PersistentDataType.INTEGER, page - 1);
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getNextPageItem(int page) {
        ItemStack item = nextPageItem.clone();
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(guiItemDataKey, PersistentDataType.INTEGER, page + 1);
        item.setItemMeta(meta);

        return item;
    }
}
