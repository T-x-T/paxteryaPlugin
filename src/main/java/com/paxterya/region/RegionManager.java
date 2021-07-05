package com.paxterya.region;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class RegionManager {

    private RegionConfigLoader configLoader;

    Map<UUID, Region> playerRegion = new HashMap<>();

    private List<Region> regions;

    public RegionManager(Plugin plugin) {
        configLoader = new RegionConfigLoader(plugin);
        regions = configLoader.loadRegions();
        startTask(plugin);
    }

    private void startTask(Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            Region newRegion = getRegion(player.getLocation());

            Region previousRegion = playerRegion.put(player.getUniqueId(), newRegion);
            if (newRegion == null && previousRegion == null) {
                return;
            } if (newRegion == null && previousRegion != null) {
                Bukkit.getPluginManager().callEvent(new RegionChangeEvent(player, previousRegion, null));
            } else if (!newRegion.equals(previousRegion)) {
                Bukkit.getPluginManager().callEvent(new RegionChangeEvent(player, previousRegion, newRegion));
            }
        }), 69, 20);
    }

    private Region getRegion(Location location) {
        for (Region region : regions) {
            if (region.contains(location)) return region;
        }
        return null;
    }
}
