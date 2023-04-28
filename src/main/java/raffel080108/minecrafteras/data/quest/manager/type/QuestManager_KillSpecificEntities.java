/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.data.quest.manager.type;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import raffel080108.minecrafteras.data.quest.manager.QuestManagerType;

import java.util.List;

public class QuestManager_KillSpecificEntities extends QuestManager_KillEntities {
    private final List<EntityType> requiredTypes;

    public QuestManager_KillSpecificEntities(int requiredAmount, List<EntityType> requiredTypes, String managedQuestName) {
        super(requiredAmount, managedQuestName);
        this.requiredTypes = requiredTypes;
    }

    @Override
    public QuestManagerType getType() {
        return QuestManagerType.KILL_SPECIFIC_ENTITIES;
    }

    @Override
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (!active || !requiredTypes.contains(event.getEntityType()))
            return;

        data++;
        displayQuestStatusUpdate();
    }
}
