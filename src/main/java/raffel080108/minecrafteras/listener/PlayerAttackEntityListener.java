/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.config.ConfigData;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import raffel080108.minecrafteras.data.sound.SoundKey;
import raffel080108.minecrafteras.data.sound.SoundsData;
import raffel080108.minecrafteras.util.UtilData;

public class PlayerAttackEntityListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.PLAYER)
            return;

        ConfigData configData = ConfigData.getInstance();
        if (event.getEntity().getPersistentDataContainer().has(UtilData.getInstance().getCampMasterKey())) {
            if (configData.isCampMasterInvincible())
                event.setCancelled(true);
            return;
        }

        if (!configData.isEntityAttackingUnlockingEnabled() || PluginData.getInstance().getAttackableEntityTypes().contains(event.getEntityType()) ||
                !(event.getDamager() instanceof Player player))
            return;

        event.setCancelled(true);
        player.playSound(SoundsData.getInstance().getSound(SoundKey.CANNOT_ATTACK_ENTITY));
        player.sendMessage(MessagesData.getInstance().getMessage(Message.CANNOT_ATTACK_ENTITY));
    }
}
