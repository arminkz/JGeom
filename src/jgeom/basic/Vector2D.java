package jgeom.basic;

public class Vector2D {
    private double dx;
    private double dy;
    private double length;

    public Vector2D(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
        this.length = 0.0D / 0.0;
    }

    public double getLength() {
        if (Double.isNaN(this.length)) {
            this.length = Math.hypot(this.dx, this.dy);
        }

        return this.length;
    }

    public double dot(Vector2D v) {
        return this.dx * v.dx + this.dy * v.dy;
    }

    public double getX() {
        return this.dx;
    }

    public double getY() {
        return this.dy;
    }

    public Vector2D add(Vector2D v) {
        return new Vector2D(this.dx + v.dx, this.dy + v.dy);
    }

    public Vector2D scale(double amount) {
        return new Vector2D(this.dx * amount, this.dy * amount);
    }

    public Vector2D normalised() {
        return this.scale(1.0D / this.getLength());
    }

    public Vector2D getNormal() {
        return new Vector2D(-this.dy, this.dx);
    }

    public String toString() {
        return this.dx + ", " + this.dy;
    }
}
