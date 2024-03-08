/*
 ItemRestrict © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
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
