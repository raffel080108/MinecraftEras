/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
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
