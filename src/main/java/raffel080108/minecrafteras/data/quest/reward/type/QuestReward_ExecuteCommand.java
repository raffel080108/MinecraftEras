/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.data.quest.reward.type;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import raffel080108.minecrafteras.data.quest.reward.QuestReward;

public class QuestReward_ExecuteCommand extends QuestReward {
    private final Command command;

    public QuestReward_ExecuteCommand(Command command) {
        this.command = command;
    }

    @Override
    public void grantReward(Player player) {
        Bukkit.dispatchCommand(command.sendAsPlayer() ? player : Bukkit.getConsoleSender(),
                command.command().replace("<player>", player.getName()));
        grantedBefore = true;
    }

    public record Command(String command, boolean sendAsPlayer) {}
}
