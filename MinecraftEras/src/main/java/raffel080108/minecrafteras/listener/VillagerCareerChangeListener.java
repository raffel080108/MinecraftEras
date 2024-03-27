/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use any contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import raffel080108.minecrafteras.util.UtilData;

public class VillagerCareerChangeListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onVillagerCareerChange(VillagerCareerChangeEvent event) {
        if (event.getEntity().getPersistentDataContainer().has(UtilData.getInstance().getCampMasterKey()))
            event.setCancelled(true);
    }
}
