/*
 ItemRestrict © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import raffel080108.minecrafteras.data.config.ConfigData;
import raffel080108.minecrafteras.util.UtilData;

public class EntityDamageListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        EntityDamageEvent.DamageCause damageCause = event.getCause();
        if (!event.getEntity().getPersistentDataContainer().has(UtilData.getInstance().getCampMasterKey()) ||
                damageCause == EntityDamageEvent.DamageCause.CUSTOM || damageCause == EntityDamageEvent.DamageCause.VOID)
            return;

        if (ConfigData.getInstance().isCampMasterInvincible())
            event.setCancelled(true);
    }
}
