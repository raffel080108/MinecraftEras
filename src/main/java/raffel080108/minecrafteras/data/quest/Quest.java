/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.data.quest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import raffel080108.minecrafteras.data.quest.manager.QuestManager;
import raffel080108.minecrafteras.data.quest.reward.QuestReward;
import raffel080108.minecrafteras.data.sound.SoundKey;
import raffel080108.minecrafteras.data.sound.SoundsData;

import java.util.List;

public record Quest(String name, int requiredEraScore, boolean allowMultiCompletion, Component detailsMessage,
                    ItemStack displayItem, QuestManager manager, List<QuestReward> rewards) {
    public void reset() {
        PluginData pluginData = PluginData.getInstance();
        manager.setActive(false);
        pluginData.decreaseActiveQuestsAmount();
        manager.setData(null);
        pluginData.getPlayersTrackingQuest().get(name).clear();
    }

    public void displayAsCompleted() {
        MessagesData messagesData = MessagesData.getInstance();
        TagResolver questNamePlaceholder = Placeholder.unparsed("quest", name);

        Bukkit.broadcast(messagesData.getMessage(Message.QUEST_COMPLETED_BROADCAST, questNamePlaceholder));

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showTitle(Title.title(messagesData.getMessage(Message.QUEST_COMPLETED_TITLE,
                            questNamePlaceholder),
                    messagesData.getMessage(Message.QUEST_COMPLETED_SUBTITLE,
                            questNamePlaceholder)));
            onlinePlayer.playSound(SoundsData.getInstance().getSound(SoundKey.QUEST_COMPLETED));
        }

        manager.setActive(false);
        PluginData.getInstance().decreaseActiveQuestsAmount();
    }
}
