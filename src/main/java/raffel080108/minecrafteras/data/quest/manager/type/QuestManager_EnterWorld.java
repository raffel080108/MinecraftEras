/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
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
