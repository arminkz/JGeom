package jgeom.basic;

import java.util.List;

public class Polygon2D {

    private List<Point2D> points;

    public Polygon2D(List<Point2D> points) {
        this.points = points;
    }

    public List<Point2D> getVertices() { return points; }

}
