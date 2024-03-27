/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.config.ConfigData;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import raffel080108.minecrafteras.data.sound.SoundKey;
import raffel080108.minecrafteras.data.sound.SoundsData;

public class BlockBreakListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!ConfigData.getInstance().isBlockMiningUnlockingEnabled() || PluginData.getInstance().getMineableBlockTypes().contains(event.getBlock().getType()))
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        player.playSound(SoundsData.getInstance().getSound(SoundKey.CANNOT_MINE_BLOCK));
        player.sendMessage(MessagesData.getInstance().getMessage(Message.CANNOT_MINE_BLOCK));
    }
}
