/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.command;

import org.bukkit.entity.LivingEntity;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class RemoveCampMasterCommand {
    @Command({"mce", "minecrafteras"})
    @Subcommand("removeCampMaster")
    @CommandPermission("minecrafteras.removeCampMaster")
    public void removeCampMasterCommand(BukkitCommandActor sender) {
        PluginData pluginData = PluginData.getInstance();
        LivingEntity campMaster = pluginData.getCampMaster();
        if (campMaster == null) {
            sender.reply(MessagesData.getInstance().getMessage(Message.CAMP_MASTER_NOT_SPAWNED));
            return;
        }

        campMaster.setHealth(0);
        pluginData.setCampMaster(null);
    }
}
