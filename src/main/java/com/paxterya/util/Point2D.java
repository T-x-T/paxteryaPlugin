package com.paxterya.util;

public class Point2D {
    double x; double y;

    public Point2D(double x, double y) {
        this.x = x; this.y = y;
    }

    public double distanceTo(Point2D p) {
        double dx = x - p.x;
        double dy = y - p.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
