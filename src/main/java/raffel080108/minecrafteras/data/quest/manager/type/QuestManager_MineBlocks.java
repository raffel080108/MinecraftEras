/*
 ItemRestrict © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.data.quest.manager.type;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.quest.manager.QuestManagerType;
import raffel080108.minecrafteras.data.quest.manager.QuestManager_NumericData;

public class QuestManager_MineBlocks extends QuestManager_NumericData {
    private final int requiredAmount;

    public QuestManager_MineBlocks(int requiredAmount, String managedQuestName) {
        super(managedQuestName);
        this.requiredAmount = requiredAmount;
    }

    @Override
    public boolean checkRequirementsMet() {
        return data >= requiredAmount;
    }

    @Override
    public QuestManagerType getType() {
        return QuestManagerType.MINE_BLOCKS;
    }

    @Override
    public void displayQuestStatusUpdate() {
        displayQuestStatusUpdate(Message.QUEST_PROGRESS_MINING, data, requiredAmount);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!active)
            return;

        data++;
        displayQuestStatusUpdate();
    }
}
