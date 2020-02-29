package jgeom.basic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class GeometryHelper {
    public static final double THRESHOLD = 1.0E-13D;

    public static Point2D getIntersectionPoint(Line2D l1, Line2D l2) {
        double t1 = l1.getIntersection(l2);
        double t2 = l2.getIntersection(l1);
        return !Double.isNaN(t1) && !Double.isNaN(t2) ? l1.getPoint(t1) : null;
    }

    public static Point2D getSegmentIntersectionPoint(Line2D l1, Line2D l2) {
        double t1 = l1.getIntersection(l2);
        double t2 = l2.getIntersection(l1);
        return !Double.isNaN(t1) && !Double.isNaN(t2) && t1 >= 0.0D && t1 <= 1.0D && t2 >= 0.0D && t2 <= 1.0D ? l1.getPoint(t1) : null;
    }

    public static boolean parallel(Line2D l1, Line2D l2) {
        double d = l1.getDirection().getX() * l2.getDirection().getY() - l1.getDirection().getY() * l2.getDirection().getX();
        return nearlyZero(d);
    }

    public static boolean contains(Line2D line, Point2D point) {
        double tx;
        double ty;
        double d;
        double xMax;
        if (nearlyZero(line.getDirection().getX())) {
            tx = point.getX() - line.getOrigin().getX();
            ty = point.getY();
            d = Math.min(line.getOrigin().getY(), line.getEndPoint().getY());
            xMax = Math.max(line.getOrigin().getY(), line.getEndPoint().getY());
            return nearlyZero(tx) && ty >= d && ty <= xMax;
        } else if (!nearlyZero(line.getDirection().getY())) {
            tx = (point.getX() - line.getOrigin().getX()) / line.getDirection().getX();
            ty = (point.getY() - line.getOrigin().getY()) / line.getDirection().getY();
            if (tx >= 0.0D && tx <= 1.0D && ty >= 0.0D && ty <= 1.0D) {
                d = tx - ty;
                return nearlyZero(d);
            } else {
                return false;
            }
        } else {
            tx = point.getY() - line.getOrigin().getY();
            ty = point.getX();
            d = Math.min(line.getOrigin().getX(), line.getEndPoint().getX());
            xMax = Math.max(line.getOrigin().getX(), line.getEndPoint().getX());
            return nearlyZero(tx) && ty >= d && ty <= xMax;
        }
    }

    public static double positionOnLine(Line2D line, Point2D point) {
        double tx;
        if (nearlyZero(line.getDirection().getX())) {
            tx = (point.getY() - line.getOrigin().getY()) / line.getDirection().getY();
            return tx;
        } else if (nearlyZero(line.getDirection().getY())) {
            tx = (point.getX() - line.getOrigin().getX()) / line.getDirection().getX();
            return tx;
        } else {
            tx = (point.getX() - line.getOrigin().getX()) / line.getDirection().getX();
            double ty = (point.getY() - line.getOrigin().getY()) / line.getDirection().getY();
            double d = tx - ty;
            return nearlyZero(d) ? tx : 0.0D / 0.0;
        }
    }

    public static double getRightAngleBetweenLines(Line2D first, Line2D second) {
        return getRightAngleBetweenVectors(first.getDirection(), second.getDirection());
    }

    public static double getLeftAngleBetweenLines(Line2D first, Line2D second) {
        return getLeftAngleBetweenVectors(first.getDirection(), second.getDirection());
    }

    public static double getAngleBetweenVectors(Vector2D first, Vector2D second) {
        Vector2D v1 = first.normalised();
        Vector2D v2 = second.normalised();
        double cos = v1.dot(v2);
        if (cos > 1.0D) {
            cos = 1.0D;
        }

        double angle = Math.toDegrees(Math.acos(cos));
        return angle;
    }

    public static double getRightAngleBetweenVectors(Vector2D first, Vector2D second) {
        double angle = getAngleBetweenVectors(first, second);
        return isRightTurn(first, second) ? angle : 360.0D - angle;
    }

    public static double getLeftAngleBetweenVectors(Vector2D first, Vector2D second) {
        double angle = getAngleBetweenVectors(first, second);
        return isRightTurn(first, second) ? 360.0D - angle : angle;
    }

    public static boolean isRightTurn(Line2D first, Line2D second) {
        return isRightTurn(first.getDirection(), second.getDirection());
    }

    public static boolean isRightTurn(Vector2D first, Vector2D second) {
        double t = first.getX() * second.getY() - first.getY() * second.getX();
        return t < 0.0D;
    }

    public static boolean nearlyZero(double d) {
        return d > -1.0E-13D && d < 1.0E-13D;
    }

    public static double computeArea(List<Point2D> vertices) {
        return Math.abs(computeAreaUnsigned(vertices));
    }

    public static Point2D computeCentroid(List<Point2D> vertices) {
        double area = computeAreaUnsigned(vertices);
        Iterator<Point2D> it = vertices.iterator();
        Point2D last = (Point2D)it.next();
        Point2D first = last;
        double xSum = 0.0D;

        double ySum;
        Point2D next;
        for(ySum = 0.0D; it.hasNext(); last = next) {
            next = (Point2D)it.next();
            double lastX = last.getX();
            double lastY = last.getY();
            double nextX = next.getX();
            double nextY = next.getY();
            xSum += (lastX + nextX) * (lastX * nextY - nextX * lastY);
            ySum += (lastY + nextY) * (lastX * nextY - nextX * lastY);
        }

        double lastX = last.getX();
        double lastY = last.getY();
        double nextX = first.getX();
        double nextY = first.getY();
        xSum += (lastX + nextX) * (lastX * nextY - nextX * lastY);
        ySum += (lastY + nextY) * (lastX * nextY - nextX * lastY);
        xSum /= 6.0D * area;
        ySum /= 6.0D * area;
        return new Point2D(xSum, ySum);
    }

    public static List<Point2D> vertexArrayToPoints(int[] vertices) {
        List<Point2D> result = new ArrayList();

        for(int i = 0; i < vertices.length; i += 2) {
            result.add(new Point2D((double)vertices[i], (double)vertices[i + 1]));
        }

        return result;
    }

    public static List<Point2D> vertexArrayToPoints(double[] vertices) {
        List<Point2D> result = new ArrayList();

        for(int i = 0; i < vertices.length; i += 2) {
            result.add(new Point2D(vertices[i], vertices[i + 1]));
        }

        return result;
    }

    public static List<Line2D> pointsToLines(List<Point2D> points) {
        return pointsToLines(points, false);
    }

    public static List<Line2D> pointsToLines(List<Point2D> points, boolean close) {
        List<Line2D> result = new ArrayList();
        Iterator<Point2D> it = points.iterator();
        Point2D first = (Point2D)it.next();

        Point2D prev;
        Point2D next;
        for(prev = first; it.hasNext(); prev = next) {
            next = (Point2D)it.next();
            result.add(new Line2D(prev, next));
        }

        if (close && !prev.equals(first)) {
            result.add(new Line2D(prev, first));
        }

        return result;
    }

    public static Point2D getClosestPoint(Line2D line, Point2D point) {
        Point2D p1 = line.getOrigin();
        Point2D p2 = line.getEndPoint();
        double u = ((point.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (point.getY() - p1.getY()) * (p2.getY() - p1.getY())) / (line.getDirection().getLength() * line.getDirection().getLength());
        return line.getPoint(u);
    }

    public static Point2D getClosestPointOnSegment(Line2D line, Point2D point) {
        Point2D p1 = line.getOrigin();
        Point2D p2 = line.getEndPoint();
        double u = ((point.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (point.getY() - p1.getY()) * (p2.getY() - p1.getY())) / (line.getDirection().getLength() * line.getDirection().getLength());
        if (u <= 0.0D) {
            return p1;
        } else {
            return u >= 1.0D ? p2 : line.getPoint(u);
        }
    }

    public static double getDistance(Point2D p1, Point2D p2) {
        return Math.hypot(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    public static Line2D clipToRectangle(Line2D line, double xMin, double yMin, double xMax, double yMax) {
        double x1 = line.getOrigin().getX();
        double y1 = line.getOrigin().getY();
        double x2 = line.getEndPoint().getX();
        double y2 = line.getEndPoint().getY();
        double tL = (xMin - x1) / (x2 - x1);
        double tR = (xMax - x1) / (x2 - x1);
        double tT = (yMax - y1) / (y2 - y1);
        double tB = (yMin - y1) / (y2 - y1);
        double tMin = 0.0D;
        double tMax = 1.0D;
        if (x1 < xMin && x2 < xMin || x1 > xMax && x2 > xMax || y1 < yMin && y2 < yMin || y1 > yMax && y2 > yMax) {
            return null;
        } else {
            if (tL > tMin && tL < tMax) {
                if (x1 < x2) {
                    tMin = tL;
                } else {
                    tMax = tL;
                }
            }

            if (tR > tMin && tR < tMax) {
                if (x1 > x2) {
                    tMin = tR;
                } else {
                    tMax = tR;
                }
            }

            if (tT > tMin && tT < tMax) {
                if (y1 > y2) {
                    tMin = tT;
                } else {
                    tMax = tT;
                }
            }

            if (tB > tMin && tB < tMax) {
                if (y1 < y2) {
                    tMin = tB;
                } else {
                    tMax = tB;
                }
            }

            return tMin > tMax ? null : new Line2D(line.getPoint(tMin), line.getPoint(tMax));
        }
    }

    private static double computeAreaUnsigned(List<Point2D> vertices) {
        Iterator<Point2D> it = vertices.iterator();
        Point2D last = (Point2D)it.next();
        Point2D first = last;

        double sum;
        Point2D next;
        for(sum = 0.0D; it.hasNext(); last = next) {
            next = (Point2D)it.next();
            double lastX = last.getX();
            double lastY = last.getY();
            double nextX = next.getX();
            double nextY = next.getY();
            sum += lastX * nextY - nextX * lastY;
        }

        double lastX = last.getX();
        double lastY = last.getY();
        double nextX = first.getX();
        double nextY = first.getY();
        sum += lastX * nextY - nextX * lastY;
        sum /= 2.0D;
        return sum;
    }
}
