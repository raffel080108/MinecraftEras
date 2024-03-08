/*
 ItemRestrict © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import raffel080108.minecrafteras.command.*;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.quest.QuestData;
import raffel080108.minecrafteras.data.quest.QuestsDataHandler;
import raffel080108.minecrafteras.listener.*;
import raffel080108.minecrafteras.listener.gui.CampMasterInteractListener;
import raffel080108.minecrafteras.listener.gui.GUIClickListener;
import raffel080108.minecrafteras.util.UtilData;
import raffel080108.minecrafteras.util.config.ConfigHandler;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public final class MinecraftEras extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDataFile();
        FileConfiguration data = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data.yml"));
        PluginData pluginData = PluginData.getInstance();
        pluginData.setDataFileConfig(data);

        UtilData.getInstance().initializeData();
        new ConfigHandler().loadConfigurations();

        Logger log = getLogger();
        log.info("Running internal setups...");
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new BlockBreakListener(), this);
        pluginManager.registerEvents(new PlayerAttackEntityListener(), this);
        pluginManager.registerEvents(new PrepareItemCraftListener(), this);
        pluginManager.registerEvents(new CampMasterInteractListener(), this);
        pluginManager.registerEvents(new GUIClickListener(), this);
        pluginManager.registerEvents(new EntityDamageListener(), this);
        pluginManager.registerEvents(new EntityDeathListener(), this);
        pluginManager.registerEvents(new VillagerCareerChangeListener(), this);

        BukkitCommandHandler commandHandler = BukkitCommandHandler.create(this);
        commandHandler.enableAdventure();
        commandHandler.register(new AddEraScoreCommand());
        commandHandler.register(new ReloadCommand());
        commandHandler.register(new DepositItemCommand());
        commandHandler.register(new SpawnCampMasterCommand());
        commandHandler.register(new RemoveCampMasterCommand());
        commandHandler.register(new CheckEraScoreCommand());
        commandHandler.register(new HelpCommand());
        commandHandler.registerBrigadier();

        log.info("Loading data...");
        String campMasterUUID = data.getString("campMasterUuid", "");
        if (!campMasterUUID.equals("")) {
            Villager campMaster = (Villager) Bukkit.getEntity(UUID.fromString(campMasterUUID));
            if (campMaster != null)
                pluginData.setCampMaster(campMaster);
            else log.warning("Could not get camp-master entity due to invalid saved UUID");
        }
        pluginData.updateCampMaster();

        int eraScore = data.getInt("eraScore");
        pluginData.setEraScore(eraScore);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        pluginData.getPreviouslyUnlockedEras().addAll(
                gson.fromJson(data.getString("previouslyUnlockedEras", "[]"),
                        new TypeToken<List<String>>(){}.getType()));

        pluginData.addEraScore(0);

        QuestsDataHandler questDataHandler = QuestsDataHandler.getInstance();
        questDataHandler.getQuestsData().putAll(
                gson.fromJson(data.getString("questsData", "[]"), new TypeToken<Map<String, QuestData>>(){}.getType()));
        questDataHandler.loadQuestsData();

        for (Map.Entry<String, UUID> entry : pluginData.getPlayersTrackingQuest().entries())
            pluginData.getTrackedQuests().put(entry.getValue(), entry.getKey());

        log.info("Plugin started!");
    }

    @Override
    public void onDisable() {
        PluginData pluginData = PluginData.getInstance();
        FileConfiguration data = pluginData.getDataFileConfig();

        Logger log = getLogger();
        log.info("Saving data...");
        saveDataFile();

        Villager campMaster = pluginData.getCampMaster();
        if (campMaster != null)
            data.set("campMasterUuid", campMaster.getUniqueId().toString());
        else data.set("campMasterUuid", "");

        data.set("eraScore", pluginData.getEraScore());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        data.set("previouslyUnlockedEras", gson.toJson(pluginData.getPreviouslyUnlockedEras(),
                new TypeToken<List<String>>(){}.getType()));

        QuestsDataHandler questDataHandler = QuestsDataHandler.getInstance();
        questDataHandler.saveQuestsData();
        data.set("questsData", gson.toJson(questDataHandler.getQuestsData(),
                new TypeToken<Map<String, QuestData>>(){}.getType()));

        try {
            data.save(new File(getDataFolder(), "data.yml"));
        } catch (IOException e) {
            e.printStackTrace();
            log.severe("Could not save data to data-file, any recent data will be lost");
        }

        log.info("Plugin stopped!");
    }

    private void saveDataFile() {
        if (!new File(getDataFolder(), "data.yml").exists())
            saveResource("data.yml", false);
    }

    public static MinecraftEras getInstance() {
        return getPlugin(MinecraftEras.class);
    }
}
