package com.paxterya.util;

import org.bukkit.Location;

public abstract class Shape {

    public boolean contains(Location location) {
        return contains(new Point2D(location.getX(), location.getZ()));
    }

    public abstract boolean contains(Point2D p);
}
