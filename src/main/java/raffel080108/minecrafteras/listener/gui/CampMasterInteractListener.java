/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.listener.gui;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import raffel080108.minecrafteras.util.GUIProvider;
import raffel080108.minecrafteras.util.UtilData;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class CampMasterInteractListener implements Listener {
    private final Map<UUID, Long> interactCooldown = new Hashtable<>();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (!entity.getPersistentDataContainer().has(UtilData.getInstance().getCampMasterKey()))
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (interactCooldown.containsKey(uuid))
            if (interactCooldown.get(uuid) + 500L > System.currentTimeMillis())
                return;

        interactCooldown.put(uuid, System.currentTimeMillis());

        Inventory gui = new GUIProvider().getGui(1, player.getUniqueId());
        if (gui == null) {
            player.sendMessage(MessagesData.getInstance().getMessage(Message.NO_QUESTS_AVAILABLE));
            player.playSound(Sound.sound(Key.key("entity.villager.no"), Sound.Source.MASTER, 1F, 1F),
                    Sound.Emitter.self());
            return;
        }

        player.openInventory(gui);
    }
}
