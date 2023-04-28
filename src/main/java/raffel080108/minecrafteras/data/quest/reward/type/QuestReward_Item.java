/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
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
