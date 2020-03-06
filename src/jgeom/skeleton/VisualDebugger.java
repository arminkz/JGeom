package jgeom.skeleton;

import jgeom.basic.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.List;

public class VisualDebugger extends JPanel implements KeyListener, MouseListener {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Polygon Skeleton Demo");
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        VisualDebugger vd = new VisualDebugger();
        frame.addMouseListener(vd);
        frame.addKeyListener(vd);
        frame.add(vd);
        frame.setVisible(true);
    }

    private List<Point2D> pts = new ArrayList<>();
    private boolean polygonClosed = false;
    private Polygon polygon;

    Polygon2D poly2d;
    PolygonSkeleton polygonSkeleton;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0,this.getWidth(),this.getHeight());
        g2d.setColor(Color.WHITE);
        if(!polygonClosed) {
            int n = pts.size();
            for (int i = 0; i < n; i++) {
                Point2D p = pts.get(i);
                g2d.fillOval((int) p.getX() - 2, (int) p.getY() - 2, 4, 4);
                if (i + 1 < n) {
                    Point2D pp = pts.get(i + 1);
                    g2d.drawLine((int) p.getX(), (int) p.getY(), (int) pp.getX(), (int) pp.getY());
                }
            }
        }else {
            g2d.drawPolygon(polygon);
            if(polygonSkeleton != null) {

                g2d.setColor(new Color(255, 0, 241));
//                for(SKVertex v : polygonSkeleton.vertices) {
//                    if (!v.getProcessed()) {
//                        g2d.fillRect((int)v.getPoint2D().getX()-5,
//                                (int)v.getPoint2D().getY()-5,
//                                10,10);
//                    }
//                }


                g2d.setColor(Color.GREEN);
                for(Line2D sk : polygonSkeleton.skeleton){
                    g2d.drawLine((int)sk.getOrigin().getX(),(int)sk.getOrigin().getY(),(int)sk.getEndPoint().getX(),(int)sk.getEndPoint().getY());
                }

                g2d.setColor(Color.RED);
//                for(IntersectionPoint intersect : polygonSkeleton.pQueue) {
//                    Point2D b = intersect.getPoint();
//                    Point2D p1 = intersect.getEdge().getOrigin().getPoint2D();
//                    Point2D p2 = intersect.getEdge().getEnd().getPoint2D();
//                    g2d.fillOval((int) b.getX() - 2, (int) b.getY() - 2, 4, 4);
//                    g2d.drawLine((int)p1.getX(),(int)p1.getY(),(int)b.getX(),(int)b.getY());
//                    g2d.drawLine((int)p2.getX(),(int)p2.getY(),(int)b.getX(),(int)b.getY());
//                    //Point2D p = poly2d.getVertices().get(i);
//                    //g2d.drawLine((int)p.getX(),(int)p.getY(),(int)(p.getX() + b.getX()),(int)(p.getY() + b.getY()));
//                    //i++;
//                }

            }
        }
    }

    private void reset() {
        pts = new ArrayList<>();
        polygonClosed = false;
        repaint();
    }

    private void createPolygon() {
        int n = pts.size();
        int xpos[] = new int[n];
        int ypos[] = new int[n];
        for (int i = 0; i < n; i++) {
            xpos[i] = (int)pts.get(i).getX();
            ypos[i] = (int)pts.get(i).getY();
        }
        polygon = new Polygon(xpos,ypos,n);
        poly2d = new Polygon2D(pts);
        polygonSkeleton = new PolygonSkeleton(poly2d);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == 'c') {
            createPolygon();
            polygonClosed = true;
            repaint();
        }
        if(e.getKeyChar() == 'r') reset();
        if(e.getKeyChar() == 'a') {
            polygonSkeleton.advance();
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!polygonClosed) {
            pts.add(new Point2D(e.getX(), e.getY()));
            repaint();
        }
    }

    @Override public void keyPressed(KeyEvent e) { }
    @Override public void keyReleased(KeyEvent e) { }
    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
}
