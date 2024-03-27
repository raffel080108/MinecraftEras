/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
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
