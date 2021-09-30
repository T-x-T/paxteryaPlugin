package com.paxterya.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Base {

    private Location location;

    private Map<UUID, String> owners;

    private String name;

    private int id;

    public String getName() {
        if (name != null) return name; // if cached, return
        StringBuilder baseName = new StringBuilder("Base of ");
        Collection<String> names = owners.values();

        int i = 0;
        for (String name : names) {
            baseName.append(name);
            if (i < names.size() - 2)
                baseName.append(", ");
            else if (i < names.size() - 1)
                baseName.append(" and ");
            i++;
        }

        this.name = baseName.toString();
        return this.name;
    }

    public void addOwner(@NotNull Player player) {
        owners.put(player.getUniqueId(), player.getName());
        name = null; // invalidate the cached name
    }

    public void removeOwner(@NotNull Player player) {
        owners.remove(player.getUniqueId());
        name = null; // invalidate the cached name
    }
}
