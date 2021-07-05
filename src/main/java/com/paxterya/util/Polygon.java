package com.paxterya.util;

import java.util.ArrayList;
import java.util.List;

public class Polygon {

    protected Polygon() {}

    private List<Point2D> corners = new ArrayList<>();

    void addCorner(Point2D p) {
        corners.add(p);
    }

    public List<Point2D> getCorners() {
        return corners;
    }

    public boolean contains(Point2D p) {
        return wn_PnPoly(p) != 0;
    }

    /** 
     * Algorithm from http://www.geomalgorithms.com.
     * 
     * Tests if a point is Left|On|Right of an infinite line.
     * 
     * @param P0 first point in the line
     * @param P1 second point in the line
     * @param P2 the point to test
     * 
     * @return >0 for P2 left of the line through P0 and P1
     *            =0 for P2  on the line
     *           <0 for P2  right of the line
    */
    private double isLeft(Point2D P0, Point2D P1, Point2D P2) {
        return ( (P1.x - P0.x) * (P2.y - P0.y)
                - (P2.x -  P0.x) * (P1.y - P0.y) );
    }

    /**
     * Algorithm from http://www.geomalgorithms.com.
     * 
     * winding number test for a point in a polygon
     * 
     * @param P the point to test
     * 
     * @return 0 if the point is outside the polygon, nonzero if inside
     */
    private int wn_PnPoly(Point2D P) {
        int n = corners.size() - 1;
        int wn = 0; // the  winding number counter

        // loop through all edges of the polygon
        for (int i=0; i<n; i++) { // edge from V[i] to  V[i+1]
            if (corners.get(i).y <= P.y) { // start y <= P.y
                if (corners.get(i + 1).y  > P.y) // an upward crossing
                    if (isLeft( corners.get(i), corners.get(i + 1), P) > 0)  // P left of  edge
                        ++wn; // have  a valid up intersect
            }
            else { // start y > P.y (no test needed)
                if (corners.get(i+1).y  <= P.y) // a downward crossing
                    if (isLeft( corners.get(i), corners.get(i + 1), P) < 0) // P right of  edge
                        --wn; // have a valid down intersect
            }
        }
        return wn;
    }
}
