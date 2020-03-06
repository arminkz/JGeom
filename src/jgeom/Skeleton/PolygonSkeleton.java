package jgeom.Skeleton;

import jgeom.basic.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PolygonSkeleton {

    public Polygon2D poly;
    public PolygonSkeleton(Polygon2D p) {
        this.poly = p;
        calcSkeleton();
    }

    public PriorityQueue<IntersectionPoint> pQueue;
    public ArrayList<IntersectionPoint> intersects;
    ArrayList<SKVertex> vertices = new ArrayList<>();
    ArrayList<Line2D> skeleton = new ArrayList<>();

    IntersectionPoint newIp;

    private IntersectionPoint getPossibleIntersectionPoint(SKVertex v) {
        Line2D rayPrev = v.getEdgeA().getOrigin().getBisectorRay();
        Line2D ray = v.getBisectorRay();
        Line2D rayNext = v.getEdgeB().getEnd().getBisectorRay();

        Point2D intersectPrev = GeometryHelper.getIntersectionPoint(rayPrev,ray);
        Point2D intersectNext = GeometryHelper.getIntersectionPoint(ray,rayNext);

        if(intersectPrev != null && intersectNext != null) {
            if (GeometryHelper.distance(v.getPoint2D(),intersectPrev) < GeometryHelper.distance(v.getPoint2D(),intersectNext)) {
                IntersectionPoint ip = new IntersectionPoint(v.getEdgeA(),intersectPrev);
                return ip;
            }else {
                IntersectionPoint ip = new IntersectionPoint(v.getEdgeB(),intersectNext);
                return ip;
            }
        }else{
            if(intersectPrev == null && intersectNext != null){
                IntersectionPoint ip = new IntersectionPoint(v.getEdgeB(),intersectNext);
                return ip;
            }else if(intersectPrev != null && intersectNext == null) {
                IntersectionPoint ip = new IntersectionPoint(v.getEdgeA(),intersectPrev);
                return ip;
            }else {
                return null;
            }
        }
    }

    private void calcBisector(SKVertex v) {
        v.setBisector(getBisector(v.getEdgeA().getLine().getDirection(),
                v.getEdgeB().getLine().getDirection().scale(-1)));
    }

    public void calcSkeleton() {
        int n = poly.getVertices().size();
        for(Point2D p2d : poly.getVertices()) {
            vertices.add(new SKVertex(p2d));
        }
        for (int i = 0; i < n; i++) {
            SKEdge in = new SKEdge(vertices.get(getCircularIndex(i-1)),vertices.get(i));
            SKEdge out = new SKEdge(vertices.get(i),vertices.get(getCircularIndex(i+1)));
            vertices.get(i).setEdgeA(in);
            vertices.get(i).setEdgeB(out);
        }

        for (int i = 0; i < n; i++) {
            SKVertex v = vertices.get(i);
            calcBisector(v);
        }

        pQueue = new PriorityQueue<>(Comparator.comparing(IntersectionPoint::getDistFromEdge));
        intersects = new ArrayList<>();

        //initialization
        for (int i = 0; i < n; i++) {
            SKVertex v = vertices.get(i);
            IntersectionPoint ip = getPossibleIntersectionPoint(v);
            if(ip != null) {
                pQueue.add(ip);
                intersects.add(ip);
            }
        }

        //step 2
//        while(!pQueue.isEmpty()){
//            advance();
//        }

    }

    public void advance() {
        if(pQueue.isEmpty()) return;
        //2a
        IntersectionPoint ip = pQueue.poll();
        SKVertex va = ip.getEdge().getOrigin();
        SKVertex vb = ip.getEdge().getEnd();
        //2b
        if(va.getProcessed() || vb.getProcessed()){
            return;
        }
        //2c
        if(va.getEdgeA().getOrigin().getEdgeA().getOrigin() == vb) {
            SKVertex vc = va.getEdgeA().getOrigin();
            //output skeleton
            skeleton.add(new Line2D(va.getPoint2D(),ip.getPoint())); //VaI
            skeleton.add(new Line2D(vb.getPoint2D(),ip.getPoint())); //VbI
            skeleton.add(new Line2D(vc.getPoint2D(),ip.getPoint())); //VcI

            va.markAsProcessed();
            vb.markAsProcessed();
            vc.markAsProcessed();
            return;
        }
        //2d
        skeleton.add(new Line2D(va.getPoint2D(),ip.getPoint())); //VaI
        skeleton.add(new Line2D(vb.getPoint2D(),ip.getPoint())); //VbI
        //2e
        va.markAsProcessed();
        vb.markAsProcessed();

        SKEdge aIn = va.getEdgeA();
        SKEdge bOut = vb.getEdgeB();

        SKVertex iv = new SKVertex(ip.getPoint());
        iv.setEdgeA(aIn);
        iv.setEdgeB(bOut);
        aIn.setEnd(iv);
        bOut.setOrigin(iv);
        //2f
        calcBisector(iv.getEdgeA().getOrigin());
        calcBisector(iv);
        calcBisector(iv.getEdgeB().getEnd());
        vertices.add(iv);

        IntersectionPoint nip = getPossibleIntersectionPoint(iv);
        if(nip != null) {
            pQueue.add(nip);
            intersects.add(nip);
            newIp = nip;
        }
    }

    private int getCircularIndex(int i) {
        int n = poly.getVertices().size();
        int r = i % n;
        if(r<0) r += n;
        return r;
    }

    private Vector2D getBisector(Vector2D v1, Vector2D v2) {
        return v1.normalised().add(v2.normalised()).normalised();
    }

}
