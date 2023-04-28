/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.data.quest.manager.type;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.quest.manager.QuestManager;
import raffel080108.minecrafteras.data.quest.manager.QuestManagerType;
import raffel080108.minecrafteras.data.quest.manager.QuestUpdateDisplayer;

import java.util.ArrayList;
import java.util.List;

public class QuestManager_GetAdvancements extends QuestManager implements QuestUpdateDisplayer {
    private final List<NamespacedKey> advancements;
    private List<NamespacedKey> completedAdvancements = new ArrayList<>();

    public QuestManager_GetAdvancements(List<NamespacedKey> advancements, String managedQuestName) {
        super(managedQuestName);
        this.advancements = advancements;
    }

    @Override
    public List<NamespacedKey> getData() {
        return completedAdvancements;
    }

    @Override
    public void setData(Object data) {
        if (data == null) {
            completedAdvancements = List.of();
            return;
        }

        try {
            completedAdvancements = (List<NamespacedKey>) data;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Invalid data type: " + data.getClass().getName(), e);
        }
    }

    @Override
    public boolean checkRequirementsMet() {
        return advancements.size() == completedAdvancements.size();
    }

    @Override
    public QuestManagerType getType() {
        return QuestManagerType.GET_ADVANCMENTS;
    }

    @Override
    public void displayQuestStatusUpdate() {
        displayQuestStatusUpdate(Message.QUEST_PROGRESS_ADVANCEMENTS,completedAdvancements.size(), advancements.size());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {
        NamespacedKey advancement = event.getAdvancement().getKey();
        if (!active || !advancements.contains(advancement) || completedAdvancements.contains(advancement))
            return;

        completedAdvancements.add(advancement);
        displayQuestStatusUpdate();
    }
}
