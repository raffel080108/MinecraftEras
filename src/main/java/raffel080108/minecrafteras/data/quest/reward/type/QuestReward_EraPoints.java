/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
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
