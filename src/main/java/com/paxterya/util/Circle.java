package com.paxterya.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class Circle extends Shape {

    private double radius;
    private Point2D center;

    @Override
    public boolean contains(Point2D p) {
        return center.distanceTo(p) < radius;
    }
}
