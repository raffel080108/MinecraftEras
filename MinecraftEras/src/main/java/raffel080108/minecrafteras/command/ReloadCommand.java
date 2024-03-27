/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.command;

import raffel080108.minecrafteras.MinecraftEras;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import raffel080108.minecrafteras.util.config.ConfigHandler;
import raffel080108.minecrafteras.data.quest.QuestsDataHandler;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.logging.Logger;

public class ReloadCommand {
    @Command({"mce", "minecrafteras"})
    @Subcommand("reload")
    @CommandPermission("minecrafteras.reload")
    public void reloadCommand(BukkitCommandActor sender) {
        QuestsDataHandler questsDataHandler = QuestsDataHandler.getInstance();

        questsDataHandler.saveQuestsData();

        Logger log = MinecraftEras.getInstance().getLogger();
        log.info("Reload initiated by " + sender.getName());
        Message statusMessage = new ConfigHandler().loadConfigurations();
        log.info("Reload complete");
        sender.reply(MessagesData.getInstance().getMessage(statusMessage));

        questsDataHandler.loadQuestsData();

        PluginData pluginData = PluginData.getInstance();
        pluginData.addEraScore(0);
        pluginData.updateCampMaster();
    }
}
