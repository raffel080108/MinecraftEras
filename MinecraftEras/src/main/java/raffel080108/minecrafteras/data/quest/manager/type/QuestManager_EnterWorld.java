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

public class QuestManager_EnterWorld extends QuestManager_BooleanData {
    private final World world;

    public QuestManager_EnterWorld(World world, String managedQuestName) {
        super(managedQuestName);
        this.world = world;
    }

    @Override
    public QuestManagerType getType() {
        return QuestManagerType.ENTER_WORLD;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void playerTeleportEvent(PlayerTeleportEvent event) {
        World destinationWorld = event.getTo().getWorld();
        if (!active || event.getFrom().getWorld().equals(destinationWorld) || !destinationWorld.equals(world))
            return;

        data = true;
        PluginData.getInstance().getQuests().get(managedQuestName).displayAsCompleted();
    }
}
