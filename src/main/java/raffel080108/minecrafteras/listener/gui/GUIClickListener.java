/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.listener.gui;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.config.ConfigData;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import raffel080108.minecrafteras.data.quest.Quest;
import raffel080108.minecrafteras.data.quest.manager.QuestManager;
import raffel080108.minecrafteras.data.quest.reward.QuestReward;
import raffel080108.minecrafteras.data.sound.SoundKey;
import raffel080108.minecrafteras.data.sound.SoundsData;
import raffel080108.minecrafteras.util.GUIProvider;
import raffel080108.minecrafteras.util.UtilData;

import javax.management.openmbean.InvalidKeyException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GUIClickListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null)
            return;

        MessagesData messagesData = MessagesData.getInstance();
        InventoryView view = event.getView();
        if (!view.title().equals(messagesData.getMessage(Message.QUESTS_GUI_TITLE)))
            return;

        event.setCancelled(true);

        if (!item.hasItemMeta())
            return;

        PersistentDataContainer dataContainer = item.getItemMeta().getPersistentDataContainer();
        UtilData utilData = UtilData.getInstance();
        NamespacedKey guiItemKey = utilData.getGuiItemKey();
        if (!dataContainer.has(guiItemKey))
            return;

        PluginData pluginData = PluginData.getInstance();
        SoundsData soundsData = SoundsData.getInstance();
        NamespacedKey dataKey = utilData.getGuiItemDataKey();
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        //noinspection DataFlowIssue
        int guiItemType = dataContainer.get(guiItemKey, PersistentDataType.INTEGER);
        ClickType clickType = event.getClick();
        if (clickType == ClickType.LEFT || clickType == ClickType.SHIFT_LEFT) {
            if (guiItemType == 1) {
                Map<String, Quest> quests = pluginData.getQuests();
                String questName = dataContainer.get(dataKey, PersistentDataType.STRING);
                if (!quests.containsKey(questName)) {
                    view.close();
                    player.sendMessage(Component.text("Internal error occurred (check the server console for details)")
                            .color(NamedTextColor.RED));
                    throw new InvalidKeyException("Quest with name \"" + questName + "\" doesn't exist");
                }

                Quest quest = pluginData.getQuests().get(questName);
                QuestManager questManager = quest.manager();
                TagResolver questNamePlaceholder = Placeholder.unparsed("quest", quest.name());
                if (questManager.checkRequirementsMet()) {
                    quest.reset();

                    player.sendMessage(messagesData.getMessage(Message.QUEST_REWARD_CLAIMED));
                    player.playSound(soundsData.getSound(SoundKey.QUEST_REWARD_CLAIMED));

                    for (QuestReward reward : quest.rewards())
                        reward.grantReward(player);

                    view.close();
                    return;
                }

                if (questManager.isActive()) {
                    quest.reset();

                    Bukkit.broadcast(messagesData.getMessage(Message.QUEST_ABORTED_BROADCAST, questNamePlaceholder));
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers())
                        onlinePlayer.playSound(soundsData.getSound(SoundKey.QUEST_ABORTED));

                    view.close();
                    return;
                }

                if (pluginData.getActiveQuestsAmount() >= ConfigData.getInstance().getMaxActiveQuests()) {
                    player.sendMessage(messagesData.getMessage(Message.MAX_QUESTS_ACTIVE));
                    player.playSound(soundsData.getSound(SoundKey.MAX_QUESTS_ACTIVE));
                    view.close();
                    return;
                }

                questManager.setActive(true);
                pluginData.increaseActiveQuestsAmount();

                Bukkit.broadcast(messagesData.getMessage(Message.QUEST_STARTED_BROADCAST,
                        Placeholder.component("quest-details", quest.detailsMessage()),
                        questNamePlaceholder));
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.showTitle(Title.title(messagesData.getMessage(Message.QUEST_STARTED_TITLE,
                                    questNamePlaceholder),
                            messagesData.getMessage(Message.QUEST_STARTED_SUBTITLE,
                                    questNamePlaceholder)));
                    onlinePlayer.playSound(soundsData.getSound(SoundKey.QUEST_STARTED), Sound.Emitter.self());
                }
                view.close();
            } else {
                //noinspection DataFlowIssue
                Inventory gui = new GUIProvider().getGui(dataContainer.get(dataKey, PersistentDataType.INTEGER), uuid);
                if (gui == null)
                    return;

                player.openInventory(gui);
            }
        } else if (clickType == ClickType.RIGHT || clickType == ClickType.SHIFT_RIGHT) {
            if (guiItemType == 2)
                return;

            Map<String, Quest> quests = pluginData.getQuests();
            String questName = dataContainer.get(dataKey, PersistentDataType.STRING);
            if (!quests.containsKey(questName)) {
                view.close();
                player.sendMessage(Component.text("Internal error occurred (check the server console for details)")
                        .color(NamedTextColor.RED));
                throw new InvalidKeyException("Quest with name " + questName + " doesn't exist");
            }

            QuestManager manager = quests.get(questName).manager();
            if (!manager.isActive() || !manager.getType().isTrackable())
                return;

            List<UUID> playersTrackingQuest = pluginData.getPlayersTrackingQuest().get(questName);
            //noinspection DataFlowIssue
            TagResolver questNamePlaceholder = Placeholder.unparsed("quest", questName);
            if (playersTrackingQuest.contains(uuid)) {
                playersTrackingQuest.remove(uuid);
                player.sendMessage(messagesData.getMessage(Message.QUEST_UNTRACKED, questNamePlaceholder));
                player.playSound(soundsData.getSound(SoundKey.QUEST_UNTRACKED));
            } else {
                Map<UUID, String> trackedQuests = pluginData.getTrackedQuests();
                if (trackedQuests.containsKey(uuid))
                    pluginData.getPlayersTrackingQuest().get(trackedQuests.get(uuid)).remove(uuid);

                playersTrackingQuest.add(uuid);
                player.sendMessage(messagesData.getMessage(Message.QUEST_TRACKED, questNamePlaceholder));
                player.playSound(soundsData.getSound(SoundKey.QUEST_TRACKED));
                trackedQuests.put(uuid, questName);
            }

            view.close();
        }
    }
}
