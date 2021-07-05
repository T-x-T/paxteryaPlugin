public class PolygonBuilder {
    
    private Polygon polygon = new Polygon();

    public PolygonBuilder addCorner(Point2D p) {
        polygon.addCorner(p);
        return this;
    }

    public PolygonBuilder addCorner(double x, double y) {
        polygon.addCorner(new Point2D(x, y));
        return this;
    }

    public Polygon build() {
        polygon.addCorner(polygon.corners.get(0));
        return polygon;
    }
}
