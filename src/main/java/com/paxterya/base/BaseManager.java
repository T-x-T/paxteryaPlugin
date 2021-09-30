package com.paxterya.base;

import com.paxterya.dynmap.BaseDrawer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class BaseManager {

    @Getter
    private List<Base> bases;

    private final Map<UUID, Base> playerBases = new HashMap<>();;

    private final int mergeDistance;

    private int nextId;

    private final AtomicBoolean hasUpdates = new AtomicBoolean();

    public BaseManager(Plugin plugin) {
        mergeDistance = plugin.getConfig().getInt("base_merge_distance", 20);
        reload(plugin);
        int periodTicks = plugin.getConfig().getInt("base_update_task_period", 60) * 20;
        Bukkit.getScheduler().runTaskTimer(plugin, getUpdateTask(plugin), periodTicks, periodTicks);
    }

    public boolean setBase(@NotNull Player player) {
        boolean removed = unsetBase(player);
        Base base = getOrCreateBaseFor(player);
        base.addOwner(player);
        playerBases.put(player.getUniqueId(), base);
        hasUpdates.compareAndExchange(false, true);
        return removed;
    }

    public boolean unsetBase(@NotNull Player player) {
        Base base = playerBases.get(player.getUniqueId());
        if (base == null) return false;
        base.removeOwner(player);
        if (base.getOwners().size() == 0) {
            bases.remove(base);
            Bukkit.getLogger().info("[paxterya] Base at " + base.getLocation().getBlockX() + " " + base.getLocation().getBlockY() + " has been abandoned");
        }
        playerBases.remove(player.getUniqueId());
        hasUpdates.compareAndExchange(false, true);
        return true;
    }

    @NotNull
    private Base getOrCreateBaseFor(Player player) {
        return bases.stream()
                .filter(b -> b.getLocation().distance(player.getLocation()) < mergeDistance)
                .findFirst()
                .orElseGet(() -> {
                    nextId++;
                    Base base = new Base(player.getLocation(), new HashMap<>(), null, nextId);
                    bases.add(base);
                    Bukkit.getLogger().info("[paxterya] A new base has been created at " + base.getLocation().getBlockX() + " " + base.getLocation().getBlockY());
                    return base;
                });
    }

    public Base getBaseOf(Player player) {
        return playerBases.get(player.getUniqueId());
    }

    public void reload(Plugin plugin) {
        if (bases != null) BaseDataLoader.saveBases(plugin, bases);
        bases = BaseDataLoader.loadBases(plugin);
        bases.forEach(base -> {
            base.getOwners().forEach((uuid, s) -> playerBases.put(uuid, base));
        });
        hasUpdates.compareAndExchange(false, true);
    }

    private Runnable getUpdateTask(Plugin plugin) {
        return () -> {
            if (hasUpdates.compareAndExchange(true, false)) {
                Bukkit.getLogger().info("[paxterya] saving bases");
                BaseDataLoader.saveBases(plugin, bases);
                BaseDrawer.drawBasesLater(plugin, bases, 32);
            }
        };
    }
}
