/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.data.quest.manager.type;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.quest.manager.QuestManagerType;
import raffel080108.minecrafteras.data.quest.manager.QuestManager_BooleanData;

public class QuestManager_EnterDimension extends QuestManager_BooleanData {
    private final World.Environment dimensionType;
    
    public QuestManager_EnterDimension(World.Environment dimensionType, String managedQuestName) {
        super(managedQuestName);
        this.dimensionType = dimensionType;
    }

    @Override
    public QuestManagerType getType() {
        return QuestManagerType.ENTER_DIMENSION;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        World destinationWorld = event.getTo().getWorld();
        if (!active || event.getFrom().getWorld().equals(destinationWorld) || !destinationWorld.getEnvironment().equals(dimensionType))
            return;

        data = true;
        PluginData.getInstance().getQuests().get(managedQuestName).displayAsCompleted();
    }
}
