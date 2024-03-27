/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.data.quest.manager;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import raffel080108.minecrafteras.MinecraftEras;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;

import java.util.UUID;

public abstract class QuestManager implements Listener {
    protected boolean active;
    protected final String managedQuestName;

    protected QuestManager(String managedQuestName) {
        this.managedQuestName = managedQuestName;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, MinecraftEras.getInstance());
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    protected void displayQuestStatusUpdate(Message message, double current, int required) {
        PluginData pluginData = PluginData.getInstance();
        for (UUID uuid : pluginData.getPlayersTrackingQuest().get(managedQuestName)) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline())
                continue;

            player.sendActionBar(MessagesData.getInstance().getMessage(message,
                    Placeholder.unparsed("current", String.valueOf((int) Math.floor(current))),
                    Placeholder.unparsed("required", String.valueOf(required)),
                    Placeholder.unparsed("quest", managedQuestName)));
        }

        if (checkRequirementsMet())
            pluginData.getQuests().get(managedQuestName).displayAsCompleted();
    }

    public abstract boolean checkRequirementsMet();
    public abstract QuestManagerType getType();
    public abstract Object getData();
    public abstract void setData(Object data);
}
