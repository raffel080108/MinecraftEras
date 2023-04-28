/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.command;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.config.ConfigData;
import raffel080108.minecrafteras.data.config.DepositableItemConfig;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import raffel080108.minecrafteras.data.sound.SoundKey;
import raffel080108.minecrafteras.data.sound.SoundsData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.Map;

public class DepositItemCommand {
    @Command({"mce", "minecrafteras"})
    @Subcommand("depositItem")
    @CommandPermission("minecrafteras.depositItem")
    public void depositItemCommand(Player sender) {
        EntityEquipment equipment = sender.getEquipment();
        ItemStack item = equipment.getItemInMainHand();
        Material itemType = equipment.getItemInMainHand().getType();
        MessagesData messagesData = MessagesData.getInstance();
        if (itemType == Material.AIR) {
            sender.sendMessage(messagesData.getMessage(Message.NOT_HOLDING_AN_ITEM));
            return;
        }

        Map<Material, DepositableItemConfig> itemDepositAmounts = ConfigData.getInstance().getItemDepositAmounts();
        if (!itemDepositAmounts.containsKey(itemType)) {
            sender.sendMessage(messagesData.getMessage(Message.CANNOT_DEPOSIT_ITEM));
            return;
        }

        PluginData pluginData = PluginData.getInstance();
        DepositableItemConfig depositableItemConfig = itemDepositAmounts.get(itemType);
        int eraScore = pluginData.getEraScore();
        int minProgression = depositableItemConfig.minProgression();
        if (eraScore < minProgression) {
            sender.sendMessage(messagesData.getMessage(Message.NOT_ENOUGH_PROGRESSION_FOR_DEPOSIT,
                    Placeholder.unparsed("min", String.valueOf(minProgression))));
            return;
        }

        int maxProgression = depositableItemConfig.maxProgression();
        if (eraScore > maxProgression && maxProgression > 0) {
            sender.sendMessage(messagesData.getMessage(Message.TOO_MUCH_PROGRESSION_FOR_DEPOSIT,
                    Placeholder.unparsed("max", String.valueOf(maxProgression))));
            return;
        }

        int depositPointsAmount = depositableItemConfig.depositAmount() * item.getAmount();
        equipment.setItemInMainHand(null);
        pluginData.addEraScore(depositPointsAmount);
        sender.sendMessage(messagesData.getMessage(Message.ITEM_DEPOSITED,
                Placeholder.unparsed("amount", String.valueOf(depositPointsAmount))));
        sender.playSound(SoundsData.getInstance().getSound(SoundKey.ITEM_DEPOSITED));
    }
}
