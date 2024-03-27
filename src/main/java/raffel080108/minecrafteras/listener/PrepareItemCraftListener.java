/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.listener;

import org.bukkit.Keyed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Recipe;
import raffel080108.minecrafteras.data.PluginData;
import raffel080108.minecrafteras.data.config.ConfigData;

public class PrepareItemCraftListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        if (recipe == null)
            return;

        if (!ConfigData.getInstance().isRecipeCraftingUnlockingEnabled() ||
                PluginData.getInstance().getCraftableRecipes().contains(((Keyed) recipe).getKey()))
            return;

        event.getInventory().setResult(null);
    }
}
