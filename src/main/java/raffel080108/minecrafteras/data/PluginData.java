/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.data;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import raffel080108.minecrafteras.data.config.ConfigData;
import raffel080108.minecrafteras.data.config.Era;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import raffel080108.minecrafteras.data.quest.Quest;
import raffel080108.minecrafteras.data.sound.SoundKey;
import raffel080108.minecrafteras.data.sound.SoundsData;

import java.util.*;

public final class PluginData {
    private static PluginData pluginDataInstance;
    private Villager campMaster;
    private final Map<String, Quest> quests = new Hashtable<>();
    private int eraScore = -1;
    private final List<Material> mineableBlockTypes = new ArrayList<>();
    private final List<EntityType> attackableEntityTypes = new ArrayList<>();
    private final List<NamespacedKey> craftableRecipes = new ArrayList<>();
    private FileConfiguration dataFileConfig;
    private int activeQuestsAmount = 0;
    private final List<String> previouslyUnlockedEras = new ArrayList<>();
    private final ListMultimap<String, UUID> playersTrackingQuest = ArrayListMultimap.create();
    private final Map<UUID, String> trackedQuests = new Hashtable<>();

    private PluginData() {}

    public static PluginData getInstance() {
        if (pluginDataInstance == null)
            pluginDataInstance = new PluginData();
            
        return pluginDataInstance;
    }
    
    public Villager getCampMaster() {
        return campMaster;
    }

    public void setCampMaster(Villager campMaster) {
        this.campMaster = campMaster;
    }

    public void updateCampMaster() {
        if (campMaster == null)
            return;

        ConfigData configData = ConfigData.getInstance();
        campMaster.customName(configData.getCampMasterDisplayName());
        campMaster.setCustomNameVisible(true);
        campMaster.setVillagerType(configData.getCampMasterType());
        campMaster.setProfession(configData.getCampMasterProfession());

        int campMasterBehaviour = configData.getCampMasterBehaviour();
        campMaster.setAware(campMasterBehaviour != 2);
        campMaster.setAI(campMasterBehaviour != 3);
    }

    public Map<String, Quest> getQuests() {
        return quests;
    }

    public void addEraScore(int amountToAdd) {
        this.eraScore += amountToAdd;

        for (Era era : ConfigData.getInstance().getEras()) {
            int eraPointsToUnlock = era.eraPointsToUnlock();
            if (eraPointsToUnlock > eraScore)
                return;

            mineableBlockTypes.addAll(era.unlockedBlockTypes());
            attackableEntityTypes.addAll(era.unlockedEntityTypes());
            craftableRecipes.addAll(era.unlockedRecipes());

            String eraName = era.name();
            if (previouslyUnlockedEras.contains(eraName))
                continue;

            TagResolver eraPlaceholder = Placeholder.unparsed("era", era.name());
            MessagesData messagesData = MessagesData.getInstance();
            Bukkit.broadcast(messagesData.getMessage(Message.ERA_UNLOCKED_BROADCAST, eraPlaceholder,
                    Placeholder.component("era-details", era.detailsMessage())));

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.showTitle(Title.title(
                        messagesData.getMessage(Message.ERA_UNLOCKED_TITLE, eraPlaceholder),
                        messagesData.getMessage(Message.ERA_UNLOCKED_SUBTITLE, eraPlaceholder)));
                player.playSound(SoundsData.getInstance().getSound(SoundKey.ERA_UNLOCKED), Sound.Emitter.self());
            }

            previouslyUnlockedEras.add(eraName);

            return;
        }
    }

    public void setEraScore(int eraScore) {
        this.eraScore = eraScore;
    }

    public int getEraScore() {
        return eraScore;
    }

    public List<Material> getMineableBlockTypes() {
        return mineableBlockTypes;
    }

    public List<EntityType> getAttackableEntityTypes() {
        return attackableEntityTypes;
    }

    public List<NamespacedKey> getCraftableRecipes() {
        return craftableRecipes;
    }

    public FileConfiguration getDataFileConfig() {
        return dataFileConfig;
    }

    public void setDataFileConfig(FileConfiguration dataFileConfig) {
        this.dataFileConfig = dataFileConfig;
    }

    public int getActiveQuestsAmount() {
        return activeQuestsAmount;
    }

    public void setActiveQuestsAmount(int activeQuestsAmount) {
        this.activeQuestsAmount = activeQuestsAmount;
    }

    public void increaseActiveQuestsAmount() {
        activeQuestsAmount++;
    }

    public void decreaseActiveQuestsAmount() {
        activeQuestsAmount--;
    }

    public List<String> getPreviouslyUnlockedEras() {
        return previouslyUnlockedEras;
    }

    public ListMultimap<String, UUID> getPlayersTrackingQuest() {
        return playersTrackingQuest;
    }

    public Map<UUID, String> getTrackedQuests() {
        return trackedQuests;
    }
}
