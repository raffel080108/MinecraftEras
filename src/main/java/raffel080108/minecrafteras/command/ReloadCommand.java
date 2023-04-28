/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
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
