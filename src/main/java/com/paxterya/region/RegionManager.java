package com.paxterya.region;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class RegionManager {

    Map<UUID, Region> playerRegion = new HashMap<>();

    private List<Region> regions;

    private BukkitTask updateTask;

    public RegionManager(Plugin plugin) {
        regions = RegionConfigLoader.loadRegions(plugin);
        updateTask = Bukkit.getScheduler().runTaskTimer(plugin, getUpdatetask(), 69, 20);
    }

    public void reload(Plugin plugin) {
        Bukkit.getScheduler().cancelTask(updateTask.getTaskId());
        regions = RegionConfigLoader.loadRegions(plugin);
        updateTask = Bukkit.getScheduler().runTaskTimer(plugin, getUpdatetask(), 69, 20);
    }

    private Region getRegion(Location location) {
        for (Region region : regions) {
            if (region.contains(location)) {
                return region.getSubRegions().stream().filter(sub -> sub.contains(location)).findAny().orElse(region);
            }
        }
        return null;
    }

    private Runnable getUpdatetask() {
        return () -> Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            Region newRegion = getRegion(player.getLocation());

            Region previousRegion = playerRegion.put(player.getUniqueId(), newRegion);
            if (newRegion == null && previousRegion == null) {
                return;
            } if (newRegion == null) {
                Bukkit.getPluginManager().callEvent(new RegionChangeEvent(player, previousRegion, null));
            } else if (!newRegion.equals(previousRegion)) {
                Bukkit.getPluginManager().callEvent(new RegionChangeEvent(player, previousRegion, newRegion));
            }
        });
    }
}
