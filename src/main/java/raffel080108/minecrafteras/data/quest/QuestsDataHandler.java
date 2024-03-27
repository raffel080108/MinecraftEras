/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use any contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.data.quest;

import com.google.common.collect.ListMultimap;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.quest.manager.QuestManager;
import raffel080108.minecrafteras.data.quest.reward.QuestReward;

import java.util.*;

public final class QuestsDataHandler {
    private static QuestsDataHandler questsDataHandlerInstance;
    private final Map<String, QuestData> questsData = new Hashtable<>();

    private QuestsDataHandler() {}

    public static QuestsDataHandler getInstance() {
        if (questsDataHandlerInstance == null)
            questsDataHandlerInstance = new QuestsDataHandler();

        return questsDataHandlerInstance;
    }

    public void saveQuestsData() {
        questsData.clear();

        PluginData pluginData = PluginData.getInstance();
        for (Quest quest : pluginData.getQuests().values()) {
            QuestManager questManager = quest.manager();
            List<UUID> trackingPlayers = pluginData.getPlayersTrackingQuest().get(quest.name());
            //noinspection DataFlowIssue
            questsData.put(quest.name(),
                    new QuestData(questManager.getType(),questManager.isActive(), questManager.getData(),
                            quest.rewards().get(0).wasGrantedBefore(),
                            Objects.requireNonNullElse(trackingPlayers, List.of())));
        }

        pluginData.setActiveQuestsAmount(0);
    }

    public void loadQuestsData() {
        PluginData pluginData = PluginData.getInstance();
        if (!questsData.isEmpty()) {
            Map<String, Quest> quests = pluginData.getQuests();
            for (Map.Entry<String, QuestData> entry : questsData.entrySet()) {
                String questName = entry.getKey();
                if (!quests.containsKey(questName))
                    continue;

                Quest quest = quests.get(questName);
                QuestData questData = entry.getValue();
                QuestManager questManager = quest.manager();
                if (!questManager.getType().equals(questData.managerType()))
                    continue;

                boolean managerActive = questData.managerActive();
                questManager.setActive(managerActive);
                if (managerActive)
                    pluginData.increaseActiveQuestsAmount();

                questManager.setData(questData.managerData());

                for (QuestReward questReward : quest.rewards())
                    questReward.setGrantedBefore(questData.questRewardGranted());

                ListMultimap<String, UUID> playersTrackingQuest = pluginData.getPlayersTrackingQuest();
                if (!playersTrackingQuest.containsKey(questName))
                    playersTrackingQuest.putAll(questName, questData.trackingPlayers());
            }
        }
    }

    public Map<String, QuestData> getQuestsData() {
        return questsData;
    }
}
