/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.data.quest.reward;

import org.bukkit.entity.Player;

public abstract class QuestReward {
    protected boolean grantedBefore;

    public boolean wasGrantedBefore() {
        return grantedBefore;
    }

    public void setGrantedBefore(boolean grantedBefore) {
        this.grantedBefore = grantedBefore;
    }

    public abstract void grantReward(Player player);
}
