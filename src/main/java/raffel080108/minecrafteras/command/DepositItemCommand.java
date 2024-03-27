/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
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
