package controller.mode;

import model.*;
import model.Shape;
import ui.CanvasArea;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

public class SelectMode extends DefaultMode {
    private CanvasArea canvas;
    private Point startPoint; // 紀錄滑鼠按下的起點
    private Point lastPoint;  // 紀錄滑鼠拖拉過程中的上一個點
    private Point anchorPoint; // 基準錨點：縮放時固定不動的那個點

    private Port targetPort = null;
    private BasicObject resizeTarget = null;
    private Shape moveTarget = null;
    private boolean isBoxSelecting = false;

    public SelectMode(CanvasArea canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startPoint = e.getPoint();
        lastPoint = e.getPoint();
        targetPort = null;
        resizeTarget = null;
        moveTarget = null;
        isBoxSelecting = false;

        // Resize
        for (Shape s : canvas.getShapes()) {
            if (s.isSelected() && s instanceof BasicObject basic) {
                targetPort = basic.findPortAt(startPoint);
                if (targetPort != null) {
                    resizeTarget = basic;
                    double ax = 1.0 - targetPort.getRelativeX();
                    double ay = 1.0 - targetPort.getRelativeY();
                    anchorPoint = new Point(
                            (int)(basic.getX() + ax * basic.getWidth()),
                            (int)(basic.getY() + ay * basic.getHeight())
                    );
                    return;
                }
            }
        }

        // Move
        for (int i = canvas.getShapes().size() - 1; i >= 0; i--) {
            Shape s = canvas.getShapes().get(i);
            if (s.contains(startPoint)) {
                moveTarget = s;
                if (!s.isSelected()) {
                    canvas.deselectAll();
                    s.setSelected(true);
                }
                canvas.moveShapeToFront(s);
                canvas.repaint();
                return;
            }
        }

        // 框選
        isBoxSelecting = true;
        canvas.deselectAll();
        canvas.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point current = e.getPoint();

        if (resizeTarget != null && targetPort != null) {
            handleCrossResize(resizeTarget, current);
        } else if (moveTarget != null) {
            int dx = current.x - lastPoint.x;
            int dy = current.y - lastPoint.y;
            moveTarget.move(dx, dy);
        }

        lastPoint = current;
        canvas.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isBoxSelecting) {
            Rectangle selectRect = getSelectionBounds();
            for (Shape s : canvas.getShapes()) {
                Rectangle shapeRect = new Rectangle(s.getX(), s.getY(), s.getWidth(), s.getHeight());
                if (selectRect.contains(shapeRect)) {
                    s.setSelected(true);
                }
            }
        }

        isBoxSelecting = false;
        resizeTarget = null;
        targetPort = null;
        moveTarget = null;
        canvas.repaint();
    }
    private void handleCrossResize(BasicObject obj, Point current) {
        int newX = Math.min(current.x, anchorPoint.x);
        int newY = Math.min(current.y, anchorPoint.y);
        int newW = Math.abs(current.x - anchorPoint.x);
        int newH = Math.abs(current.y - anchorPoint.y);

        if (newW < 20) {
            newW = 20;
            if (current.x < anchorPoint.x) newX = anchorPoint.x - 20;
        }
        if (newH < 20) {
            newH = 20;
            if (current.y < anchorPoint.y) newY = anchorPoint.y - 20;
        }

        obj.setX(newX);
        obj.setY(newY);
        obj.setWidth(newW);
        obj.setHeight(newH);
    }
    @Override
    public void paint(Graphics g) {
        if (isBoxSelecting && startPoint != null && lastPoint != null) {
            Graphics2D g2d = (Graphics2D) g;
            float[] dash = {5.0f};
            g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
            g2d.setColor(new Color(0, 100, 255, 100));
            g2d.draw(getSelectionBounds());
            g2d.setStroke(new BasicStroke(1.0f));
        }
    }

    private Rectangle getSelectionBounds() {
        int x = Math.min(startPoint.x, lastPoint.x);
        int y = Math.min(startPoint.y, lastPoint.y);
        int w = Math.abs(startPoint.x - lastPoint.x);
        int h = Math.abs(startPoint.y - lastPoint.y);
        return new Rectangle(x, y, w, h);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        boolean changed = false;
        for (Shape s : canvas.getShapes()) {
            boolean inside = s.contains(e.getPoint());
            if (s.isHovered() != inside) {
                s.setHovered(inside);
                changed = true;
            }
        }
        if (changed) canvas.repaint();
    }
}