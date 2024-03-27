/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
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
