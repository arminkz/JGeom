package jgeom.Skeleton;

import jgeom.basic.GeometryHelper;
import jgeom.basic.Point2D;

class IntersectionPoint {
    private Point2D intersect;
    private SKEdge edge;

    public IntersectionPoint(SKEdge edge, Point2D intersect) {
        this.edge = edge;
        this.intersect = intersect;
    }

    public Point2D getPoint() {
        return intersect;
    }

    public SKEdge getEdge() {
        return edge;
    }

    public double getDistFromEdge() {
        return GeometryHelper.distance(intersect,edge.getLine());
    }

}