/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
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
