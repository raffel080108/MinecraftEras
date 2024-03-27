/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.data.quest.manager.type;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.quest.manager.QuestManagerType;
import raffel080108.minecrafteras.data.quest.manager.QuestManager_NumericData;

public class QuestManager_TravelBlocks extends QuestManager_NumericData {
    private final int requiredAmount;

    public QuestManager_TravelBlocks(int requiredAmount, String managedQuestName) {
        super(managedQuestName);
        this.requiredAmount = requiredAmount;
    }

    @Override
    public boolean checkRequirementsMet() {
        return data >= requiredAmount;
    }

    @Override
    public QuestManagerType getType() {
        return QuestManagerType.TRAVEL_BLOCKS;
    }

    @Override
    public void displayQuestStatusUpdate() {
        displayQuestStatusUpdate(Message.QUEST_PROGRESS_TRAVELING, data, requiredAmount);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void playerMoveEvent(PlayerMoveEvent event) {
        Location startLocation = event.getFrom();
        Location targetLocation = event.getTo();
        if (event.isCancelled() || !active || (startLocation.getX() == targetLocation.getX() && startLocation.getZ() == targetLocation.getZ()))
            return;

        data += Math.sqrt(Math.pow((startLocation.getX() - targetLocation.getX()), 2d) +
                Math.pow((startLocation.getZ() - targetLocation.getZ()), 2d));
        displayQuestStatusUpdate();
    }
}
