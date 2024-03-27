/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.command;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class CheckEraScoreCommand {
    @Command({"mce", "minecrafteras"})
    @Subcommand("checkEraScore")
    @CommandPermission("minecrafteras.checkEraPoints")
    public void checkEraScoreCommand(BukkitCommandActor sender) {
        sender.reply(MessagesData.getInstance().getMessage(Message.CURRENT_ERA_SCORE,
                Placeholder.unparsed("amount", String.valueOf(PluginData.getInstance().getEraScore()))));
    }
}
