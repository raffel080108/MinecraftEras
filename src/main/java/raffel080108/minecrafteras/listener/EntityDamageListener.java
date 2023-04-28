/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import raffel080108.minecrafteras.data.config.ConfigData;
import raffel080108.minecrafteras.util.UtilData;

public class EntityDamageListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        EntityDamageEvent.DamageCause damageCause = event.getCause();
        if (!event.getEntity().getPersistentDataContainer().has(UtilData.getInstance().getCampMasterKey()) ||
                damageCause == EntityDamageEvent.DamageCause.CUSTOM || damageCause == EntityDamageEvent.DamageCause.VOID)
            return;

        if (ConfigData.getInstance().isCampMasterInvincible())
            event.setCancelled(true);
    }
}
