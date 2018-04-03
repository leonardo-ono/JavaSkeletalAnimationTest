/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import math.Vec4;
import skeletal_animation.Bone;
import skeletal_animation.Vertex;

/**
 *
 * @author leonardo
 */
public class View extends JPanel implements MouseMotionListener {

    private Vec4 mouse = new Vec4();
    
    private Bone boneRoot;
    private List<Bone> bonesList = new ArrayList<Bone>();
    private List<Vertex> vertices = new ArrayList<Vertex>();
    private List<Vertex[]> faces = new ArrayList<Vertex[]>();
    
    public View() {
        addMouseMotionListener(this);
        
        boneRoot = new Bone(Bone.Type.ROOT);
        
        Bone boneJointA = new Bone(Bone.Type.JOINT);
        boneJointA.setParent(boneRoot);
        boneJointA.getOffset().set(100, 0, 0, 1);
        boneRoot.getChildren().add(boneJointA);

        Bone boneJointB = new Bone(Bone.Type.JOINT);
        boneJointB.setParent(boneJointA);
        boneJointB.getOffset().set(100, 0, 0, 1);
        boneJointA.getChildren().add(boneJointB);
        
        Bone boneEnd = new Bone(Bone.Type.END);
        boneEnd.setParent(boneJointB);
        boneEnd.getOffset().set(100, 0, 0, 1);
        boneJointB.getChildren().add(boneEnd);
        
        boneRoot.fillBonesList(bonesList);
        boneRoot.calculateInverseTransformation();
        
        // double px, double py, Bone b0, Bone b1, double w0, double w1
        
        Vertex v0 = new Vertex(0, 30, boneRoot, boneRoot, 0.5, 0.5);
        Vertex v1 = new Vertex(0, -30, boneRoot, boneRoot, 0.5, 0.5);
        
        Vertex v2 = new Vertex(80, 30, boneRoot, boneJointA, 0.75, 0.25);
        Vertex v3 = new Vertex(80, -30, boneRoot, boneJointA, 0.75, 0.25);
        
        Vertex v4 = new Vertex(100, 30, boneRoot, boneJointA, 0.5, 0.5);
        Vertex v5 = new Vertex(100, -30, boneRoot, boneJointA, 0.5, 0.5);
        
        Vertex v6 = new Vertex(120, 30, boneRoot, boneJointA, 0.25, 0.75);
        Vertex v7 = new Vertex(120, -30, boneRoot, boneJointA, 0.25, 0.75);
        
        Vertex v8 = new Vertex(180, 30, boneJointA, boneJointB, 0.75, 0.25);
        Vertex v9 = new Vertex(180, -30, boneJointA, boneJointB, 0.75, 0.25);
        
        Vertex v10 = new Vertex(200, 30, boneJointA, boneJointB, 0.5, 0.5);
        Vertex v11 = new Vertex(200, -30, boneJointA, boneJointB, 0.5, 0.5);
        
        Vertex v12 = new Vertex(220, 30, boneJointA, boneJointB, 0.25, 0.75);
        Vertex v13 = new Vertex(220, -30, boneJointA, boneJointB, 0.25, 0.75);

        Vertex v14 = new Vertex(300, 30, boneEnd, boneEnd, 0, 1);
        Vertex v15 = new Vertex(300, -30, boneEnd, boneEnd, 0, 1);
        
        vertices.add(v0);
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);
        vertices.add(v4);
        vertices.add(v5);
        vertices.add(v6);
        vertices.add(v7);
        vertices.add(v8);
        vertices.add(v9);
        vertices.add(v10);
        vertices.add(v11);
        vertices.add(v12);
        vertices.add(v13);
        vertices.add(v14);
        vertices.add(v15);
        
        faces.add(new Vertex[] { v0, v1, v3, v2 });
        faces.add(new Vertex[] { v2, v3, v5, v4 });
        faces.add(new Vertex[] { v4, v5, v7, v6 });
        faces.add(new Vertex[] { v6, v7, v9, v8 });
        faces.add(new Vertex[] { v8, v9, v11, v10 });
        faces.add(new Vertex[] { v10, v11, v13, v12 });
        faces.add(new Vertex[] { v12, v13, v15, v14 });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
        g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
        g2d.translate(getWidth() / 2, getHeight() / 2);
        update();
        draw((Graphics2D) g);
        drawFaces(g2d);
        drawVertices(g2d);
    }

    private void update() {
        data[0] = mouse.x * 0;
        data[1] = mouse.y * 0.03;
        data[2] = mouse.x * 0.03;
        boneRoot.setPose(data);
        for (Vertex v : vertices) {
            v.calculate();
        }
    }
    
    private double[] data = { 0, 0, 0, 0 };
    
    private void draw(Graphics2D g) {
        for (Bone parent : bonesList) {
            int x1 = (int) parent.getPosition().getX();
            int y1 = (int) parent.getPosition().getY();
            for (Bone child : parent.getChildren()) {
                int x2 = (int) child.getPosition().getX();
                int y2 = (int) child.getPosition().getY();
                g.setColor(Color.BLACK);
                g.drawLine(x1, y1, x2, y2);
                g.setColor(Color.RED);
                g.fillOval((int) (x2 - 3), (int) (y2 - 3), 6, 6);
            }
            g.setColor(Color.RED);
            g.fillOval((int) (x1 - 3), (int) (y1 - 3), 6, 6);
        }
    }
    
    private final Polygon polygon = new Polygon();
    
    private void drawFaces(Graphics2D g) {
        g.setColor(Color.BLUE);
        for (Vertex[] vs : faces) {
            polygon.reset();
            for (Vertex v : vs) {
                polygon.addPoint((int) v.positionTransf.getX(), (int) v.positionTransf.getY());
            }
            g.draw(polygon);
        }
    }
    
    private void drawVertices(Graphics2D g) {
        for (Vertex v : vertices) {
            g.setColor(Color.BLUE);
            int x1 = (int) v.positionTransf.getX();
            int y1 = (int) v.positionTransf.getY();
            g.fillOval((int) (x1 - 3), (int) (y1 - 3), 6, 6);
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        mouse.set(e.getX(), e.getY(), 0, 1);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouse.set(e.getX(), e.getY(), 0, 1);
        repaint();
    }
    
}
