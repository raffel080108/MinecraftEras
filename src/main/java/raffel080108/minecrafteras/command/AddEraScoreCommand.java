/*
 ItemRestrict © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.command;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class AddEraScoreCommand {
    @Command({"mce", "minecrafteras"})
    @Subcommand("addEraScore")
    @CommandPermission("minecrafteras.addEraScore")
    public void addEraPointsCommand(BukkitCommandActor sender, @Named("amount") @Range(min = 1) int amountToAdd) {
        PluginData.getInstance().addEraScore(amountToAdd);
        sender.reply(MessagesData.getInstance().getMessage(Message.ERA_SCORE_ADDED,
                Placeholder.unparsed("amount", String.valueOf(amountToAdd))));
    }
}
