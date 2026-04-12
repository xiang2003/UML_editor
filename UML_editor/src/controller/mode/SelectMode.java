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

    // 狀態 Flag
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

        // 1. Resize 判定
        for (Shape s : canvas.getShapes()) {
            if (s.isSelected() && s instanceof BasicObject basic) {
                targetPort = basic.findPortAt(startPoint);
                if (targetPort != null) {
                    resizeTarget = basic;
                    // --- Alternatives F.2 關鍵：計算對角的錨點 ---
                    // 假設我們目前的 Port 是基於相對比例 (0.0~1.0)
                    // 錨點就是該 Port 的反方向。例如：拖動右下(1,1)，錨點就是左上(0,0)
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

        // 2. Move 判定
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

        // 3. 框選判定
        isBoxSelecting = true;
        canvas.deselectAll();
        canvas.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point current = e.getPoint();

        if (resizeTarget != null && targetPort != null) {
            // --- 執行支援交叉反向的 Resize ---
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
            // 結束框選，執行選取判定
            Rectangle selectRect = getSelectionBounds();
            for (Shape s : canvas.getShapes()) {
                Rectangle shapeRect = new Rectangle(s.getX(), s.getY(), s.getWidth(), s.getHeight());
                if (selectRect.contains(shapeRect)) {
                    s.setSelected(true);
                }
            }
        }

        // 放開後清空所有臨時狀態
        isBoxSelecting = false;
        resizeTarget = null;
        targetPort = null;
        moveTarget = null;
        canvas.repaint();
    }
    private void handleCrossResize(BasicObject obj, Point current) {
        // 1. 計算新的邊界 (由基準錨點與滑鼠目前位置決定)
        int newX = Math.min(current.x, anchorPoint.x);
        int newY = Math.min(current.y, anchorPoint.y);
        int newW = Math.abs(current.x - anchorPoint.x);
        int newH = Math.abs(current.y - anchorPoint.y);

        // 2. Use Case F.3 最小尺寸限制
        if (newW < 20) {
            newW = 20;
            // 如果寬度被強制限制在 20，且滑鼠是在錨點左邊，x 座標需要重新計算
            if (current.x < anchorPoint.x) newX = anchorPoint.x - 20;
        }
        if (newH < 20) {
            newH = 20;
            if (current.y < anchorPoint.y) newY = anchorPoint.y - 20;
        }

        // 3. 更新物件位置與大小 (自動轉換基準座標)
        obj.setX(newX);
        obj.setY(newY);
        obj.setWidth(newW);
        obj.setHeight(newH);
    }
    @Override
    public void paint(Graphics g) {
        // 只有在框選模式下才畫虛線框
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
        // 處理 Hover 效果
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