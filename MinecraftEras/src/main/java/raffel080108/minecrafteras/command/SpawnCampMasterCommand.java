/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.command;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataType;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import raffel080108.minecrafteras.data.sound.SoundKey;
import raffel080108.minecrafteras.data.sound.SoundsData;
import raffel080108.minecrafteras.util.UtilData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class SpawnCampMasterCommand {
    @Command({"mce", "minecrafteras"})
    @Subcommand("spawnCampMaster")
    @CommandPermission("minecrafteras.spawnCampMaster")
    public void spawnCampMasterCommand(BukkitCommandActor sender, @Named("x") @Optional Double x, @Named("y") @Optional Double y,
                                       @Named("z") @Optional Double z, @Named("world") @Optional String worldName) {
        PluginData pluginData = PluginData.getInstance();
        MessagesData messagesData = MessagesData.getInstance();
        if (pluginData.getCampMaster() != null) {
            sender.reply(messagesData.getMessage(Message.CAMP_MASTER_ALREADY_SPAWNED));
            return;
        }

        boolean senderIsPlayer = sender.isPlayer();
        Player player = sender.getAsPlayer();
        Location location;
        if (x == null || y == null || z == null) {
            if (!senderIsPlayer) {
                sender.reply(messagesData.getMessage(Message.MISSING_ARGUMENTS,
                        Placeholder.unparsed("missing-args", "location")));
                return;
            }

            //noinspection DataFlowIssue
            location = player.getLocation();
        } else if (worldName == null) {
            if (!senderIsPlayer) {
                sender.reply(messagesData.getMessage(Message.MISSING_ARGUMENTS,
                        Placeholder.unparsed("missing-args", "world")));
                return;
            }

            //noinspection DataFlowIssue
            location = new Location(player.getWorld(), x, y, z);
        } else {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                sender.reply(messagesData.getMessage(Message.WORLD_DOESNT_EXIST,
                        Placeholder.unparsed("world", worldName)));
            }

            location = new Location(world, x, y, z);
        }

        World world = location.getWorld();
        Villager campMaster = (Villager) world.spawnEntity(location, EntityType.VILLAGER);
        campMaster.getPersistentDataContainer().set(UtilData.getInstance().getCampMasterKey(), PersistentDataType.STRING, "true");
        campMaster.setVillagerLevel(2);

        pluginData.setCampMaster(campMaster);
        pluginData.updateCampMaster();

        world.playSound(SoundsData.getInstance().getSound(SoundKey.CAMP_MASTER_SPAWNED), campMaster);
        Bukkit.broadcast(messagesData.getMessage(Message.CAMP_MASTER_SPAWNED_BROADCAST,
                Placeholder.unparsed("world", world.getName()),
                Placeholder.unparsed("location", ((int) Math.floor(location.getX()))
                        + " " + ((int) Math.floor(location.getY())) + " " + ((int) Math.floor(location.getZ())))));
    }
}
