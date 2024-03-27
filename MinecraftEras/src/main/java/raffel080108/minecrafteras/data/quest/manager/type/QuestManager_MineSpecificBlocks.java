/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.data.quest.manager.type;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import raffel080108.minecrafteras.data.quest.manager.QuestManagerType;

import java.util.List;

public class QuestManager_MineSpecificBlocks extends QuestManager_MineBlocks {
    private final List<Material> requiredMaterials;

    public QuestManager_MineSpecificBlocks(int requiredAmount, List<Material> requiredMaterials, String managedQuestName) {
        super(requiredAmount, managedQuestName);
        this.requiredMaterials = requiredMaterials;
    }

    @Override
    public QuestManagerType getType() {
        return QuestManagerType.MINE_SPECIFIC_BLOCKS;
    }

    @Override
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!active || !requiredMaterials.contains(event.getBlock().getType()))
            return;

        data++;
        displayQuestStatusUpdate();
    }
}
