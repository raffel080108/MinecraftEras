/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
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
