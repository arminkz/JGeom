package jgeom.Skeleton;

import jgeom.basic.Line2D;

public class SKEdge {

    SKVertex a;
    SKVertex b;
    Line2D line;

    public SKEdge(SKVertex a, SKVertex b) {
        this.a = a;
        this.b = b;
        this.line = new Line2D(a.getPoint2D(),b.getPoint2D());
    }

    public SKVertex getOrigin() {
        return a;
    }

    public SKVertex getEnd() {
        return b;
    }

    public Line2D getLine() {
        return line;
    }

    public void setOrigin(SKVertex a) {
        this.a = a;
        this.line = new Line2D(a.getPoint2D(),b.getPoint2D());
    }

    public void setEnd(SKVertex b) {
        this.b = b;
        this.line = new Line2D(a.getPoint2D(),b.getPoint2D());
    }

}
