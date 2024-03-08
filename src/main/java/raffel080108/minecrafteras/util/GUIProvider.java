/*
 ItemRestrict © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import raffel080108.minecrafteras.data.quest.Quest;
import raffel080108.minecrafteras.data.quest.manager.QuestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GUIProvider {
    private final MessagesData messagesData = MessagesData.getInstance();
    private final UtilData utilData = UtilData.getInstance();
    private final PluginData pluginData = PluginData.getInstance();

    public Inventory getGui(int page, UUID uuid) {
        if ((page - 1) * 17 > pluginData.getQuests().size())
            return null;

        int startIndex = (page - 1) * 17;
        int index = startIndex;
        int guiIndex = 0;
        List<Quest> quests = new ArrayList<>(pluginData.getQuests().values());
        Inventory gui = Bukkit.createInventory(null, 27, messagesData.getMessage(Message.QUESTS_GUI_TITLE));
        while (guiIndex <= 17 && index < quests.size()) {
            Quest quest = quests.get(index);
            index++;

            if ((!quest.allowMultiCompletion() && quest.rewards().get(0).wasGrantedBefore()) ||
                    pluginData.getEraScore() < quest.requiredEraScore())
                continue;

            Component loreAddition;
            QuestManager questManager = quest.manager();
            if (questManager.checkRequirementsMet())
                loreAddition = messagesData.getMessage(Message.QUEST_GUI_ADDED_LORE_COMPLETED);
            else if (questManager.isActive())
                loreAddition = messagesData.getMessage(Message.QUEST_GUI_ADDED_LORE_ACTIVE);
            else loreAddition = messagesData.getMessage(Message.QUEST_GUI_ADDED_LORE_NOT_ACTIVE);
            loreAddition = loreAddition.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);

            List<Component> loreAdditions = new ArrayList<>(List.of(loreAddition));
            String questName = quest.name();
            if (quest.manager().isActive() && questManager.getType().isTrackable())
                loreAdditions.add((pluginData.getPlayersTrackingQuest().get(questName).contains(uuid)
                        ? messagesData.getMessage(Message.QUEST_GUI_ADDED_LORE_TRACKED)
                        : messagesData.getMessage(Message.QUEST_GUI_ADDED_LORE_NOT_TRACKED))
                        .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));

            ItemStack item = quest.displayItem().clone();
            ItemMeta meta = item.getItemMeta();
            List<Component> modifiedLore;
            if (meta.hasLore()) {
                //noinspection DataFlowIssue
                modifiedLore = new ArrayList<>(meta.lore());
                modifiedLore.addAll(loreAdditions);
            } else modifiedLore = loreAdditions;
            meta.lore(modifiedLore);

            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            dataContainer.set(utilData.getGuiItemKey(), PersistentDataType.INTEGER, 1);
            dataContainer.set(utilData.getGuiItemDataKey(), PersistentDataType.STRING, questName);
            item.setItemMeta(meta);

            gui.setItem(guiIndex, item);
            guiIndex++;
        }

        if (startIndex > 18)
            gui.setItem(18, utilData.getPreviousPageItem(page));
        if (guiIndex == 17)
            gui.setItem(26, utilData.getNextPageItem(page));

        if (guiIndex == 0)
            return null;
        else return gui;
    }
}
