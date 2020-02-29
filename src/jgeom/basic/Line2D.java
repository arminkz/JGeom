package jgeom.basic;

public class Line2D {
    private Point2D origin;
    private Point2D end;
    private Vector2D direction;

    public Line2D(Point2D origin, Vector2D direction) {
        this.origin = origin;
        this.direction = direction;
        this.end = origin.plus(direction);
        this.calculateDirection();
    }

    public Line2D(Point2D origin, Point2D end) {
        this.origin = origin;
        this.end = end;
        this.direction = end.minus(origin);
    }

    public Line2D(double x, double y, double dx, double dy) {
        this(new Point2D(x, y), new Vector2D(dx, dy));
    }

    public Point2D getPoint(double t) {
        return this.origin.translate(t * this.direction.getX(), t * this.direction.getY());
    }

    public Point2D getOrigin() {
        return this.origin;
    }

    public Point2D getEndPoint() {
        return this.end;
    }

    public Vector2D getDirection() {
        return this.direction;
    }

    public String toString() {
        return "Line from " + this.origin + " towards " + this.end + " (direction = " + this.direction + ")";
    }

    public double getIntersection(Line2D other) {
        double bxax = this.direction.getX();
        double dycy = other.direction.getY();
        double byay = this.direction.getY();
        double dxcx = other.direction.getX();
        double cxax = other.origin.getX() - this.origin.getX();
        double cyay = other.origin.getY() - this.origin.getY();
        double d = bxax * dycy - byay * dxcx;
        double t = cxax * dycy - cyay * dxcx;
        return GeometryHelper.nearlyZero(d) ? 0.0D / 0.0 : t / d;
    }

    public void setOrigin(Point2D origin) {
        this.origin = origin;
        this.calculateDirection();
    }

    public void setEnd(Point2D end2) {
        this.end = end2;
        this.calculateDirection();
    }

    private void calculateDirection() {
        this.direction = this.end.minus(this.origin);
    }

}
