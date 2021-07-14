package com.paxterya.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class Rectangle extends Shape{

    private double x1;
    private double y1;
    private double x2;
    private double y2;

    public Rectangle(Point2D p1, Point2D p2) {
        x1 = p1.x; y1 = p1.y; x2 = p2.x; y2 = p2.y;
    }

    @Override
    public boolean contains(Point2D p) {
        return p.x > x1 && p.x <= x2 && p.y > y1 && p.y <= y2;
    }

    public List<Point2D> getCorners() {
        return Arrays.asList(new Point2D(x1, y1), new Point2D(x2, y2));
    }
}
