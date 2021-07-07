package com.paxterya.region;

import com.paxterya.util.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

/**
 * Region is a generic area in the world defined by a 2d-shape with a name and a color
 */
@Data
@AllArgsConstructor
public class Region {

    private String id;

    private String name;

    private String dimension;

    private String color;

    private RegionType type;

    private Shape area;

    public boolean contains(Location location) {
        return location.getWorld().getName().equals(dimension) && area.contains(location);
    }

    public boolean equals(Region region) {
        return region != null && region.getId().equals(this.id);
    }

    public int hashCode() { return id.hashCode(); }
}
