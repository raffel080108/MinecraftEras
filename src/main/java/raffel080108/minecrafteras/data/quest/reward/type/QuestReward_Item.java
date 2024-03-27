/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.data.quest.reward.type;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import raffel080108.minecrafteras.data.quest.reward.QuestReward;

public class QuestReward_Item extends QuestReward {
    private final ItemStack rewardItem;

    public QuestReward_Item(ItemStack rewardItem) {
        this.rewardItem = rewardItem;
    }

    @Override
    public void grantReward(Player player) {
        if (!player.getInventory().addItem(rewardItem).isEmpty())
            player.getWorld().dropItem(player.getLocation(), rewardItem);
        grantedBefore = true;
    }
}
