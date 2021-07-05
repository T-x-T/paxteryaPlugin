package com.paxterya.region;

import com.paxterya.util.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;

/**
 * Region is a generic area in the world defined by a 2d-shape with a name and a color
 */
@Data
@AllArgsConstructor
public class Region {

    private String name;

    /**
     * The color associated with the region to be used in chat messages for example
     */
    private TextColor color;

    private Shape area;

    public boolean contains(Location location) {
        return area.contains(location);
    }

    public boolean equals(Region region) {
        return region != null && region.getName().equals(this.name);
    }
}
