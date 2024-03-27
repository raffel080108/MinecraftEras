/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.listener.gui;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import raffel080108.minecrafteras.data.message.Message;
import raffel080108.minecrafteras.data.message.MessagesData;
import raffel080108.minecrafteras.util.GUIProvider;
import raffel080108.minecrafteras.util.UtilData;

import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class CampMasterInteractListener implements Listener {
    private final Map<UUID, Long> interactCooldown = new Hashtable<>();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractAtEntity(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (!entity.getPersistentDataContainer().has(UtilData.getInstance().getCampMasterKey()))
            return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (interactCooldown.containsKey(uuid))
            if (interactCooldown.get(uuid) + 500L > System.currentTimeMillis())
                return;

        interactCooldown.put(uuid, System.currentTimeMillis());

        Inventory gui = new GUIProvider().getGui(1, player.getUniqueId());
        if (gui == null) {
            player.sendMessage(MessagesData.getInstance().getMessage(Message.NO_QUESTS_AVAILABLE));
            player.playSound(Sound.sound(Key.key("entity.villager.no"), Sound.Source.MASTER, 1F, 1F),
                    Sound.Emitter.self());
            return;
        }

        player.openInventory(gui);
    }
}
