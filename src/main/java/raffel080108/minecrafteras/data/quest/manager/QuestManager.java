/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
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
