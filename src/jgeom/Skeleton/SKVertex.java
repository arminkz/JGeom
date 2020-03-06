package jgeom.Skeleton;

import jgeom.basic.Line2D;
import jgeom.basic.Point2D;
import jgeom.basic.Vector2D;

public class SKVertex {

    private Point2D point;
    private Boolean isProcessed = false;

    private SKEdge a;
    private SKEdge b;

    private Vector2D bisector;

    public SKVertex(Point2D point) {
        this.point = point;
    }

    public void setEdgeA(SKEdge a) {
        this.a = a;
    }

    public void setEdgeB(SKEdge b) {
        this.b = b;
    }

    public void setBisector(Vector2D bisector) {
        this.bisector = bisector;
    }

    public SKEdge getEdgeA() {
        return a;
    }

    public SKEdge getEdgeB() {
        return b;
    }

    public Vector2D getBisector() {
        return bisector;
    }

    public Line2D getBisectorRay() {
        return new Line2D(point,bisector);
    }

    public void markAsProcessed() {
        isProcessed = true;
    }

    public Point2D getPoint2D() {
        return point;
    }

    public boolean getProcessed() {
        return isProcessed;
    }
}
