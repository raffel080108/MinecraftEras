/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.data.quest.reward.type;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import raffel080108.minecrafteras.data.quest.reward.QuestReward;

public class QuestReward_BroadcastMessage extends QuestReward {
    private final String message;

    public QuestReward_BroadcastMessage(String message) {
        this.message = message;
    }

    @Override
    public void grantReward(Player player) {
        Bukkit.broadcast(MiniMessage.miniMessage().deserialize(message, Placeholder.unparsed("player", player.getName())));
        grantedBefore = true;
    }
}
