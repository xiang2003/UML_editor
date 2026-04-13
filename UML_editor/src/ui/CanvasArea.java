package ui;

import controller.mode.*;
import model.CompositeObject;
import model.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CanvasArea extends JPanel {
    private List<Shape> shapes = new ArrayList<>();
    private Mode currentMode;

    public CanvasArea() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        this.currentMode = new SelectMode(this);

        MouseAdapter listener = new MouseAdapter() {
            @Override public void mousePressed(java.awt.event.MouseEvent e) { currentMode.mousePressed(e); }
            @Override public void mouseReleased(java.awt.event.MouseEvent e) { currentMode.mouseReleased(e); }
            @Override public void mouseDragged(java.awt.event.MouseEvent e) { currentMode.mouseDragged(e); }
            @Override public void mouseMoved(java.awt.event.MouseEvent e) { currentMode.mouseMoved(e); }
        };
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public void setMode(Mode mode) {
        deselectAll();

        for (Shape s : shapes) {
            s.setHovered(false);
        }

        this.currentMode = mode;

        repaint();
    }
    public void addShape(Shape s) {
        shapes.add(s);
        repaint();
    }

    public List<Shape> getShapes() { return shapes; }

    public void deselectAll() {
        for (Shape s : shapes) s.setSelected(false);
        repaint();
    }

    public void moveShapeToFront(Shape s) {
        if (shapes.remove(s)) {
            shapes.add(s);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape s : shapes) s.draw(g);

        currentMode.paint(g);
    }
    public void groupSelected() {
        List<Shape> selected = shapes.stream()
                .filter(Shape::isSelected)
                .collect(Collectors.toList());

        if (selected.size() < 2) {
            System.out.println("Grouping requires at least 2 objects.");
            return;
        }

        CompositeObject composite = new CompositeObject();
        for (Shape s : selected) {
            composite.addMember(s);
            shapes.remove(s);
        }

        composite.setSelected(true);
        shapes.add(composite);

        repaint();
    }

    public void ungroupSelected() {
        List<Shape> selected = shapes.stream()
                .filter(Shape::isSelected)
                .collect(Collectors.toList());

        if (selected.size() != 1 || !(selected.get(0) instanceof CompositeObject)) {
            System.out.println("Ungrouping requires exactly 1 composite object.");
            return;
        }

        CompositeObject target = (CompositeObject) selected.get(0);


        shapes.remove(target);
        for (Shape member : target.getMembers()) {
            member.setSelected(true);
            shapes.add(member);
        }

        repaint();
    }
}
