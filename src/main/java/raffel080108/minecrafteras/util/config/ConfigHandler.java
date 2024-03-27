/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.util.config;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import raffel080108.minecrafteras.MinecraftEras;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.config.ConfigData;
import raffel080108.minecrafteras.data.config.DepositableItemConfig;
import raffel080108.minecrafteras.data.config.Era;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import raffel080108.minecrafteras.data.quest.Quest;
import raffel080108.minecrafteras.data.quest.manager.QuestManager;
import raffel080108.minecrafteras.data.quest.manager.type.*;
import raffel080108.minecrafteras.data.quest.reward.QuestReward;
import raffel080108.minecrafteras.data.quest.reward.type.*;
import raffel080108.minecrafteras.data.sound.SoundKey;
import raffel080108.minecrafteras.data.sound.SoundsData;

import javax.management.openmbean.InvalidKeyException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public final class ConfigHandler {
    public Message loadConfigurations() {
        Logger log = MinecraftEras.getInstance().getLogger();
        log.info("Loading configurations...");
        FileConfiguration config = getConfig("config.yml");
        if (config == null)
            return Message.RELOAD_ERROR_CONFIG;

        ConfigurationSection questsConfig = config.getConfigurationSection("quests");
        if (questsConfig == null) {
            log.severe("Could not find configuration section \"quests\"");
            return Message.RELOAD_ERROR_CONFIG;
        }

        Map<String, Quest> quests = PluginData.getInstance().getQuests();
        for (Quest quest : quests.values())
            quest.manager().unregister();
        quests.clear();

        MiniMessage miniMessage = MiniMessage.miniMessage();
        for (String questKey : questsConfig.getKeys(false)) {
            ConfigurationSection questParams = questsConfig.getConfigurationSection(questKey);
            if (questParams == null) {
                log.warning("Could not find parameters for quest " + questKey);
                continue;
            }

            int eraPointsRequired = questParams.getInt("era-score-required");
            if (eraPointsRequired < 0) {
                log.warning("Found invalid value for \"era-score-required\" for quest " + questKey);
                continue;
            }

            ConfigurationSection displayItemParams = questParams.getConfigurationSection("display-item");
            if (displayItemParams == null) {
                log.warning("Could not find parameter \"display-item\" for quest " + questKey);
                continue;
            }

            Component detailsMessage = miniMessage.deserialize(questParams.getString("quest-details", ""));

            ItemStack displayItem = getItemFromParams(displayItemParams, detailsMessage, questKey);
            if (displayItem == null)
                continue;

            ConfigurationSection questManagerParams = questParams.getConfigurationSection("quest-type");
            if (questManagerParams == null) {
                log.warning("Could not find parameters for quest-type for quest " + questKey);
                continue;
            }
            String questManagerParamsPath = questManagerParams.getCurrentPath();

            String questTypeName = questManagerParams.getString("type");
            if (questTypeName == null) {
                log.warning("Could not find parameter \"type\" at " + questManagerParamsPath);
                continue;
            }

            int amountForQuestManager = 0;
            switch (questTypeName) {
                case "MINE_BLOCKS", "MINE_SPECIFIC_BLOCKS", "KILL_ENTITIES", "KILL_SPECIFIC_ENTITIES", "TRAVEL_BLOCKS" -> {
                    amountForQuestManager = questManagerParams.getInt("amount");
                    if (amountForQuestManager < 1) {
                        log.warning("Found invalid value for parameter \"amount\" at " + questManagerParamsPath);
                        continue;
                    }
                }
            }

            QuestManager questManager;
            switch (questTypeName) {
                case "MINE_BLOCKS" -> questManager = new QuestManager_MineBlocks(amountForQuestManager, questKey);
                case "MINE_SPECIFIC_BLOCKS" -> {
                    List<String> blockTypesNames = questManagerParams.getStringList("block-types");
                    if (blockTypesNames.size() == 0) {
                        log.warning("Could not find parameter \"block-types\" at " + questManagerParamsPath);
                        continue;
                    }

                    questManager = new QuestManager_MineSpecificBlocks(amountForQuestManager,
                            getMaterialList(blockTypesNames, questManagerParamsPath), questKey);
                }
                case "KILL_ENTITIES" -> questManager = new QuestManager_KillEntities(amountForQuestManager,
                        questKey);
                case "KILL_SPECIFIC_ENTITIES" -> {
                    List<String> entityTypeNames = questManagerParams.getStringList("entity-types");
                    if (entityTypeNames.size() == 0) {
                        log.warning("Could not find parameter \"entity-types\" at " + questManagerParamsPath);
                        continue;
                    }

                    questManager = new QuestManager_KillSpecificEntities(amountForQuestManager,
                            getEntityTypeList(entityTypeNames, questManagerParamsPath), questKey);
                }
                case "TRAVEL_BLOCKS" -> questManager = new QuestManager_TravelBlocks(amountForQuestManager,
                        questKey);
                case "ENTER_WORLD" -> {
                    String worldName = questManagerParams.getString("world-name");
                    if (worldName == null) {
                        log.warning("Could not find parameter \"world-name\" at " + questManagerParamsPath);
                        continue;
                    }

                    World world = Bukkit.getWorld(worldName);
                    if (world == null) {
                        log.warning("Found invalid/non-existent world-name at " + questManagerParamsPath);
                        continue;
                    }

                    questManager = new QuestManager_EnterWorld(world, questKey);
                }
                case "ENTER_DIMENSION" -> {
                    String dimensionTypeName = questManagerParams.getString("dimension-type");
                    if (dimensionTypeName == null) {
                        log.warning("Could not find parameter \"dimension-type\" at " + questManagerParamsPath);
                        continue;
                    }

                    World.Environment dimensionType;
                    try {
                        dimensionType = World.Environment.valueOf(dimensionTypeName);
                    } catch (IllegalArgumentException e) {
                        log.warning("Found invalid/non-existent dimension-type at " + questManagerParamsPath);
                        continue;
                    }

                    questManager = new QuestManager_EnterDimension(dimensionType, questKey);
                }
                case "GET_ADVANCEMENTS" -> {
                    List<String> advancementNames = questManagerParams.getStringList("advancements");
                    if (advancementNames.size() == 0) {
                        log.warning("Could not find parameter \"advancements\" at " + questManagerParamsPath);
                        continue;
                    }

                    List<NamespacedKey> advancements = new ArrayList<>();
                    for (String advancementName : advancementNames)
                        advancements.add(NamespacedKey.minecraft(advancementName.toLowerCase(Locale.ENGLISH)));

                    questManager = new QuestManager_GetAdvancements(advancements, questKey);
                }
                default -> {
                    log.warning("Found invalid quest-type for quest " + questKey);
                    continue;
                }
            }

            ConfigurationSection questRewardsConfig = questParams.getConfigurationSection("quest-rewards");
            if (questRewardsConfig == null) {
                log.warning("Could not find parameter \"quest-rewards\" for quest " + questKey);
                continue;
            }

            List<QuestReward> questRewards = new ArrayList<>();
            for (String rewardKey : questRewardsConfig.getKeys(false)) {
                ConfigurationSection questRewardParams = questRewardsConfig.getConfigurationSection(rewardKey);
                if (questRewardParams == null) {
                    log.warning("Could not find parameters for quest-reward at " + questRewardsConfig.getCurrentPath() + "." + rewardKey);
                    continue;
                }
                String questRewardParamsPath = questRewardParams.getCurrentPath();

                String rewardTypeName = questRewardParams.getString("type");
                if (rewardTypeName == null) {
                    log.warning("Could not find parameter \"type\" at " + questRewardParamsPath);
                    continue;
                }

                QuestReward questReward;
                switch (rewardTypeName) {
                    case "EXECUTE_COMMAND" -> {
                        String command = questRewardParams.getString("command");
                        if (command == null) {
                            log.warning("Could not find parameter \"command\" at " + questRewardParamsPath);
                            continue;
                        }

                        questReward = new QuestReward_ExecuteCommand(new QuestReward_ExecuteCommand.Command(command,
                                questRewardParams.getBoolean("send-as-player")));
                    }
                    case "ITEM" -> {
                        ConfigurationSection itemParams = questRewardParams.getConfigurationSection("item");
                        if (itemParams == null) {
                            log.warning("Could not find parameter \"item\" at " + questRewardParamsPath);
                            continue;
                        }

                        ItemStack item = getItemFromParams(itemParams, null, null);
                        if (item == null)
                            continue;

                        questReward = new QuestReward_Item(item);
                    }
                    case "ERA_POINTS" -> {
                        int amount = questRewardParams.getInt("amount");
                        if (amount < 1) {
                            log.warning("Found invalid value for parameter \"amount\" at " + questRewardParamsPath);
                            continue;
                        }

                        questReward = new QuestReward_EraPoints(amount);
                    }
                    case "BROADCAST_MESSAGE" -> {
                        String message = questRewardParams.getString("message");
                        if (message == null) {
                            log.warning("Could not find parameter \"message\" at " + questRewardParamsPath);
                            continue;
                        }

                        questReward = new QuestReward_BroadcastMessage(message);
                    }
                    default -> {
                        log.warning("Found invalid quest-reward for quest " + rewardKey);
                        continue;
                    }
                }

                questRewards.add(questReward);
            }

            if (questRewards.isEmpty())
                continue;

            questManager.register();
            quests.put(questKey, new Quest(questKey, eraPointsRequired, questParams.getBoolean("allow-multi-completion"),
                    detailsMessage, displayItem, questManager, questRewards));
        }

        int maxActiveQuests = config.getInt("max-active-quests", 1);
        if (maxActiveQuests < 1) {
            log.warning("Found invalid value for parameter \"max-active-quests\" in config, using default value of 1");
            maxActiveQuests = 1;
        }

        ConfigData configData = ConfigData.getInstance();
        configData.setMaxActiveQuests(maxActiveQuests);

        FileConfiguration messagesConfig = getConfig("messages.yml");
        if (messagesConfig == null)
            return Message.RELOAD_ERROR_MESSAGES;

        FileConfiguration defaultMessagesConfig = getDefaultConfig("messages.yml");
        if (defaultMessagesConfig != null)
            messagesConfig.setDefaults(defaultMessagesConfig);

        Map<Message, String> messages = MessagesData.getInstance().getMessages();
        for (String key : messagesConfig.getKeys(false)) {
            try {
                messages.put(Message.valueOf(key), messagesConfig.getString(key));
            } catch (IllegalArgumentException e) {
                log.warning("Found unknown messages key \"" + key + "\"");
            }
        }

        if (defaultMessagesConfig != null)
            for (String key : defaultMessagesConfig.getKeys(false)) {
                Message message = Message.valueOf(key);
                if (!messages.containsKey(message))
                    messages.put(message, defaultMessagesConfig.getString(key));
            }

        FileConfiguration soundsConfig = getConfig("sounds.yml");
        if (soundsConfig == null)
            return Message.RELOAD_ERROR_SOUNDS;

        FileConfiguration defaultSoundsConfig = getDefaultConfig("sounds.yml");
        if (defaultSoundsConfig != null)
            soundsConfig.setDefaults(defaultSoundsConfig);

        Map<SoundKey, Sound> sounds = SoundsData.getInstance().getSounds();
        for (String key : soundsConfig.getKeys(false)) {
            ConfigurationSection soundParams = soundsConfig.getConfigurationSection(key);
            if (soundParams == null) {
                log.warning("Could not find parameters or default parameters for sound " + key);
                continue;
            }

            String soundName = soundParams.getString("key");
            if (soundName == null) {
                log.warning("Could not find value or default value for parameter \"key\" for sound " + key);
                continue;
            }

            float volume;
            try {
                volume = Float.parseFloat(soundParams.getString("volume", "1"));
            } catch (NumberFormatException e) {
                log.warning("Found invalid value for parameter \"volume\" for sound " + key);
                continue;
            }

            if (volume <= 0F) {
                log.warning("Found invalid value for parameter \"volume\" for sound " + key);
                continue;
            }

            float pitch;
            try {
                pitch = Float.parseFloat(soundParams.getString("pitch", "1"));
            } catch (NumberFormatException e) {
                log.warning("Found invalid value for parameter \"pitch\" for sound " + key);
                continue;
            }

            if (pitch <= 0F) {
                log.warning("Found invalid value for parameter \"pitch\" for sound " + key);
                continue;
            }

            try {
                sounds.put(SoundKey.valueOf(key), Sound.sound(Key.key(soundName), Sound.Source.MASTER, volume, pitch));
            } catch (IllegalArgumentException e) {
                log.warning("Found unknown sounds key \"" + key + "\"");
            }
        }

        if (defaultSoundsConfig != null)
            for (String key : defaultSoundsConfig.getKeys(false)) {
                SoundKey soundKey = SoundKey.valueOf(key);
                ConfigurationSection soundParams = defaultSoundsConfig.getConfigurationSection(key);
                if (!sounds.containsKey(soundKey))
                    //noinspection DataFlowIssue
                    sounds.put(soundKey, Sound.sound(Key.key(soundParams.getString("key")), Sound.Source.MASTER, soundParams.getInt("volume"), soundParams.getInt("pitch")));
            }

        ConfigurationSection eraSettings = config.getConfigurationSection("era-settings");
        if (eraSettings == null) {
            log.severe("Could not find parameters \"era-settings\" in config");
            return Message.RELOAD_ERROR_CONFIG;
        }

        configData.setBlockMiningUnlockingEnabled(eraSettings.getBoolean("enable-block-mining-unlocking"));
        configData.setEntityAttackingUnlockingEnabled(eraSettings.getBoolean("enable-entity-attacking-unlocking"));
        configData.setRecipeCraftingUnlockingEnabled(eraSettings.getBoolean("enable-recipe-crafting-unlocking"));

        ConfigurationSection erasConfig = config.getConfigurationSection("eras");
        if (erasConfig == null) {
            log.severe("Could not find parameters \"eras\" in config");
            return Message.RELOAD_ERROR_CONFIG;
        }

        List<Era> eras = configData.getEras();
        List<Era> erasToAdd = new ArrayList<>();
        for (String key : erasConfig.getKeys(false)) {
            ConfigurationSection eraParams = erasConfig.getConfigurationSection(key);
            if (eraParams == null) {
                log.warning("Could not find parameters for era " + key);
                continue;
            }

            int requiredEraPoints = eraParams.getInt("required-era-score");
            if (requiredEraPoints < 0) {
                log.warning("Found invalid value for parameter \"required-era-score\" for era " + key);
                continue;
            }

            String eraParamsPath = eraParams.getCurrentPath();
            List<NamespacedKey> recipes = new ArrayList<>();
            for (String recipeName : eraParams.getStringList("unlocked-craftable-recipes")) {
                NamespacedKey recipeKey = NamespacedKey.minecraft(recipeName.toLowerCase(Locale.ENGLISH));
                Recipe recipe = Bukkit.getRecipe(recipeKey);
                if (recipe == null) {
                    log.warning("Found invalid recipe-name \"" + recipeName + "\" at " + eraParamsPath);
                    continue;
                }

                recipes.add(recipeKey);
            }

            Era era = new Era(key, miniMessage.deserialize(eraParams.getString("era-details", "")),
                    requiredEraPoints, recipes,
                    getMaterialList(eraParams.getStringList("unlocked-mineable-block-types"), eraParamsPath),
                    getEntityTypeList(eraParams.getStringList("unlocked-attackable-entity-types"),
                            eraParamsPath));
            erasToAdd.add(era);
        }
        eras.clear();
        eras.addAll(erasToAdd);

        ConfigurationSection depositableItems = config.getConfigurationSection("depositable-items");
        if (depositableItems == null) {
            log.severe("Could not find parameter \"depositable-items\" in config");
            return Message.RELOAD_ERROR_CONFIG;
        }

        Map<Material, DepositableItemConfig> itemDepositAmounts = configData.getItemDepositAmounts();
        itemDepositAmounts.clear();
        for (String key : depositableItems.getKeys(false)) {
            ConfigurationSection depositableItemParams = depositableItems.getConfigurationSection(key);
            if (depositableItemParams == null) {
                log.warning("Could not find parameters for depositable-item at " + depositableItems.getCurrentPath() + "." + key);
                continue;
            }
            String depositableItemParamsPath = depositableItemParams.getCurrentPath();

            String materialName = depositableItemParams.getString("material");
            if (materialName == null) {
                log.warning("Could not find parameter \"material\" at " + depositableItemParamsPath);
                continue;
            }
            Material material = Material.matchMaterial(materialName);
            if (material == null) {
                log.warning("Found invalid value for parameter \"material\" at " + depositableItemParamsPath);
            }

            int pointsAmount = depositableItemParams.getInt("era-score");
            if (pointsAmount < 1) {
                log.warning("Found invalid or missing value for parameter \"era-score\" at " + depositableItemParamsPath);
                continue;
            }

            int minProgression = depositableItemParams.getInt("min-progression");
            if (minProgression < 0) {
                log.warning("Found invalid value for parameter \"min-progression\" at " + depositableItemParamsPath);
                continue;
            }

            int maxProgression = depositableItemParams.getInt("max-progression");
            if (maxProgression < 0) {
                log.warning("Found invalid value for parameter \"max-progression\" at " + depositableItemParamsPath);
                continue;
            }

            itemDepositAmounts.put(material,
                    new DepositableItemConfig(pointsAmount, minProgression, maxProgression));
        }

        ConfigurationSection campMasterSettings = config.getConfigurationSection("camp-master-settings");
        if (campMasterSettings == null) {
            log.severe("Could not find parameter \"camp-master-settings\" in config");
            return Message.RELOAD_ERROR_CONFIG;
        }

        int campMasterBehaviour = campMasterSettings.getInt("behaviour", 1);
        if (campMasterBehaviour < 1 || campMasterBehaviour > 3) {
            log.warning("Found invalid value for parameter \"behaviour\" in camp-master-settings, using default value of 1");
            campMasterBehaviour = 1;
        }

        configData.setCampMasterBehaviour(campMasterBehaviour);
        configData.setCampMasterInvincible(campMasterSettings.getBoolean("invincible", true));
        configData.setCampMasterDisplayName(miniMessage.deserialize(campMasterSettings.getString("display-name", "")));

        ConfigurationSection campMasterAppearanceParams = campMasterSettings.getConfigurationSection("appearance");
        if (campMasterAppearanceParams == null) {
            log.severe("Could not find parameter \"appearance\" in camp-master-settings");
            return Message.RELOAD_ERROR_CONFIG;
        }
        String campMasterAppearanceParamsPath = campMasterAppearanceParams.getCurrentPath();

        Villager.Type campMasterType;
        try {
            campMasterType = Villager.Type.valueOf(campMasterAppearanceParams.getString("type", "PLAINS").toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            campMasterType = Villager.Type.PLAINS;
            log.warning("Found invalid value for parameter \"type\" at " + campMasterAppearanceParamsPath);
        }
        configData.setCampMasterType(campMasterType);

        Villager.Profession campMasterProfession;
        try {
            campMasterProfession = Villager.Profession.valueOf(campMasterAppearanceParams.getString("profession", "NITWIT").toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            campMasterProfession = Villager.Profession.NITWIT;
            log.warning("Found invalid value for parameter \"profession\" at " + campMasterAppearanceParamsPath);
        }

        if (campMasterProfession == Villager.Profession.NONE)
            campMasterProfession = Villager.Profession.NITWIT;

        configData.setCampMasterProfession(campMasterProfession);

        return Message.RELOAD_SUCCESS;
    }

    private FileConfiguration getConfig(String configName) {
        MinecraftEras main = MinecraftEras.getInstance();
        Logger log = main.getLogger();

        if (!new File(main.getDataFolder(), configName).exists())
            main.saveResource(configName, false);
        File configFile = new File(main.getDataFolder(), configName);

        try {
            new YamlConfiguration().load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            try {
                Files.copy(Paths.get(configFile.getPath()),
                        Paths.get(new File(main.getDataFolder(), "old_" + configName).getPath()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e2) {
                e2.printStackTrace();
                log.severe("Invalid configuration detected - Backup failed");
                return null;
            }

            main.saveResource(configName, true);
            log.warning("Invalid configuration for " + configName + " detected - Current configuration was backed up to old_" + configName + " and a new file containing the default configuration generated");
        }

        //##########
        //Config-versions are hard-coded and are updated manually
        List<ConfigVersion> configVersionKeys = List.of();
        //##########

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        FileConfiguration defaultConfig = getDefaultConfig(configName);
        int configVersion = config.getInt("config-version");
        if (defaultConfig != null && configVersion > 0) {
            int defaultConfigVersion = defaultConfig.getInt("config-version");
            if (configVersion < defaultConfigVersion) {
                int configVersionIndex = defaultConfigVersion - 2;
                //noinspection ConstantValue
                if (configVersionKeys.size() <= configVersionIndex)
                    throw new InvalidKeyException();

                for (String key : configVersionKeys.get(configVersionIndex).updatedKeys().get(configName)) {
                    if (config.get(key) == null) {
                        config.set(key, defaultConfig.get(key));
                        config.setComments(key, defaultConfig.getComments(key));
                    }
                }
                config.set("config-version", defaultConfigVersion);

                try {
                    config.save(configFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        config.set("config-version", null);
        return config;
    }

    private FileConfiguration getDefaultConfig(String configName) {
        FileConfiguration defaultConfig;
        InputStream stream = MinecraftEras.getInstance().getResource(configName);
        if (stream != null)
            defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
        else {
            MinecraftEras.getInstance().getLogger().warning("Could not get default configuration for " + configName);
            return null;
        }

        defaultConfig.set("config-version", null);
        return defaultConfig;
    }

    private ItemStack getItemFromParams(ConfigurationSection itemParams, Component questDetailsPlaceholder, String questName) {
        Logger log = MinecraftEras.getInstance().getLogger();

        //noinspection DataFlowIssue
        String parentPath = itemParams.getParent().getCurrentPath();

        String materialString = itemParams.getString("material");
        if (materialString == null) {
            log.severe("Could not find parameter material for item at " + parentPath);
            return null;
        }
        Material material = Material.matchMaterial(materialString);
        if (material == null) {
            log.severe("Found invalid value for parameter material for item at " + parentPath + "/");
            return null;
        }

        ItemStack item = new ItemStack(material, Math.max(itemParams.getInt("amount"), 1));
        ItemMeta meta = item.getItemMeta();

        MiniMessage miniMessage = MiniMessage.miniMessage();
        String nameConfig = itemParams.getString("name");
        if (nameConfig != null)
            meta.displayName(miniMessage.deserialize(nameConfig)
                    .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        else if (questName != null)
            meta.displayName(Component.text(questName)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                    .replaceText(TextReplacementConfig.builder().match("_").replacement(" ").build()));

        List<Component> lore = new ArrayList<>();
        List<String> loreConfig = itemParams.getStringList("lore");
        if (loreConfig.size() > 0) {
            for (String loreLine : loreConfig)
                lore.add((questDetailsPlaceholder != null
                        ? miniMessage.deserialize(loreLine, Placeholder.component("quest-details", questDetailsPlaceholder))
                        : miniMessage.deserialize(loreLine))
                        .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        }
        meta.lore(lore);

        ConfigurationSection enchants = itemParams.getConfigurationSection("enchantments");
        if (enchants != null)
            for (String enchantString : enchants.getKeys(false)) {
                enchantString = enchantString.toLowerCase(Locale.ROOT);
                Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantString.toLowerCase(Locale.ENGLISH)));
                if (enchant == null) {
                    log.warning("Found invalid enchantment \"" + enchantString + "\" at path " + enchants.getCurrentPath());
                    continue;
                }

                meta.addEnchant(enchant, enchants.getInt(enchantString, 1), true);
            }

        int customModelData = itemParams.getInt("custom-model-data");
        if (customModelData != 0)
            meta.setCustomModelData(customModelData);

        if (itemParams.getBoolean("hide-enchants"))
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);

        return item;
    }

    private List<Material> getMaterialList(List<String> materialNames, String path) {
        List<Material> blockTypes = new ArrayList<>();
        for (String materialName : materialNames) {
            Material material = Material.matchMaterial(materialName);
            if (material == null) {
                MinecraftEras.getInstance().getLogger().warning("Found invalid block-type name \"" + materialName + "\" at " + path);
                continue;
            }

            blockTypes.add(material);
        }

        return blockTypes;
    }

    private List<EntityType> getEntityTypeList(List<String> entityTypeNames, String path) {
        List<EntityType> entityTypes = new ArrayList<>();
        for (String entityTypeName : entityTypeNames) {
            EntityType material;
            try {
                material = EntityType.valueOf(entityTypeName.toUpperCase(Locale.ENGLISH));
            } catch (IllegalArgumentException e) {
                MinecraftEras.getInstance().getLogger().warning("Found invalid entity-type name \"" + entityTypeName + "\" at " + path);
                continue;
            }

            entityTypes.add(material);
        }

        return entityTypes;
    }
}
