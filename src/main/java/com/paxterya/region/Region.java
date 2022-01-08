package com.paxterya.region;

import com.paxterya.util.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

/**
 * Region is a generic area in the world defined by a 2d-shape with a name and a color
 */
@Data
@AllArgsConstructor
public class Region {

    private String id;

    private String name;

    private String dimension;

    private RegionType type;

    private Shape area;

    private int minY, maxY;

    private List<Region> subRegions;

    private Map<String, String> args;

    public boolean contains(Location location) {
        return location.getWorld().getName().equals(dimension)
                && location.getBlockY() <= maxY
                && location.getBlockY() >= minY
                && area.contains(location);
    }

    public boolean equals(Region region) {
        return region != null && region.getId().equals(this.id);
    }

    public int hashCode() { return id.hashCode(); }

    public String getOrDefault(String key, String def) {
        return args.getOrDefault(key, def);
    }
}
