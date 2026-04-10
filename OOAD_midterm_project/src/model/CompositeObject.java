package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CompositeObject extends Shape {
    // 組合模式的核心：持有一個 Shape 清單，Shape 可以是 BasicObject 或另一個 CompositeObject
    private List<Shape> members = new ArrayList<>();

    public CompositeObject() {
        // 初始位置與大小會由成員決定
        super(0, 0, 0, 0);
    }

    /**
     * Use Case D: 將選取的物件組合起來
     */
    public void addMember(Shape shape) {
        members.add(shape);
        updateBounds(); // 每次加入新成員都要重新計算「最小包絡矩形」
    }

    /**
     * Use Case D: 解除群組時使用
     */
    public List<Shape> getMembers() {
        return members;
    }

    /**
     * 核心邏輯：計算所有成員的聯集範圍 (Bounding Box)
     */
    public void updateBounds() {
        if (members.isEmpty()) return;

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Shape s : members) {
            minX = Math.min(minX, s.x);
            minY = Math.min(minY, s.y);
            maxX = Math.max(maxX, s.x + s.width);
            maxY = Math.max(maxY, s.y + s.height);
        }

        this.x = minX;
        this.y = minY;
        this.width = maxX - minX;
        this.height = maxY - minY;
    }

    @Override
    public void draw(Graphics g) {
        // 1. 畫出所有成員 (利用多型，不論成員是 Rect 還是 Oval)
        for (Shape s : members) {
            s.draw(g);
        }

        // 2. Use Case C: 如果 Composite 被選取，畫出虛線外框 (不顯示 Ports)
        if (isSelected) {
            Graphics2D g2d = (Graphics2D) g;
            float[] dash = {5.0f};
            g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
            g2d.setColor(Color.BLUE);
            g2d.drawRect(x, y, width, height);
            // 恢復畫筆
            g2d.setStroke(new BasicStroke(1.0f));
        }
    }

    /**
     * Use Case E: 移動 Composite 物件
     */
    @Override
    public void move(int dx, int dy) {
        // 自己移動座標
        this.x += dx;
        this.y += dy;

        // 關鍵：遞迴通知所有成員也跟著位移
        for (Shape s : members) {
            s.move(dx, dy);
        }
    }

    @Override
    public boolean contains(Point p) {
        // 判斷點是否落在組合範圍矩形內
        return new Rectangle(x, y, width, height).contains(p);
    }

    /**
     * 重寫 setSelected：當群組被選取/取消選取時，
     * 通常內部成員的選取狀態應連動或被隱藏。
     */
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        // 根據 PDF 規格，選取群組時僅顯示外框，所以我們可以將成員的 isSelected 設為 false
        for (Shape s : members) {
            s.setSelected(false);
        }
    }
}