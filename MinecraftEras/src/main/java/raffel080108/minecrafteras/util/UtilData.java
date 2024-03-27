/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use any contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
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
