/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.data.quest.reward.type;

import org.bukkit.entity.Player;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.quest.reward.QuestReward;

public class QuestReward_EraPoints extends QuestReward {
    private final int eraScoreRewardAmount;

    public QuestReward_EraPoints(int eraScoreRewardAmount) {
        this.eraScoreRewardAmount = eraScoreRewardAmount;
    }

    @Override
    public void grantReward(Player player) {
        PluginData.getInstance().addEraScore(eraScoreRewardAmount);
        grantedBefore = true;
    }
}
