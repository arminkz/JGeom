package jgeom.basic;

public class Point2D {
    private double x;
    private double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public Point2D translate(double dx, double dy) {
        return new Point2D(this.x + dx, this.y + dy);
    }

    public Vector2D minus(Point2D p) {
        return new Vector2D(this.x - p.x, this.y - p.y);
    }

    public Point2D plus(Vector2D v) {
        return new Point2D(this.x + v.getX(), this.y + v.getY());
    }

    public boolean equals(Object o) {
        if (!(o instanceof Point2D)) {
            return false;
        } else {
            return this.x == ((Point2D)o).x && this.y == ((Point2D)o).y;
        }
    }

    public int hashCode() {
        return (new Double(this.x)).hashCode() ^ (new Double(this.y)).hashCode();
    }

    public String toString() {
        return this.x + " , " + this.y;
    }

}
