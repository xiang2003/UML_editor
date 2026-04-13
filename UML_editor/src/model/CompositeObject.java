package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CompositeObject extends Shape {
    private List<Shape> members;

    public CompositeObject() {
        // 初始位置與大小會由成員決定
        super(0, 0, 0, 0);
        members = new ArrayList<>();
    }

    public void addMember(Shape shape) {
        members.add(shape);
        // 每次加入新成員都要重新計算「最小包絡矩形」
        updateBounds();
    }

    public List<Shape> getMembers() {
        return members;
    }

     //計算所有成員的聯集範圍 (Bounding Box)
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
        for (Shape s : members) {
            s.draw(g);
        }

        // 畫出虛線外框
        if (isSelected) {
            Graphics2D g2d = (Graphics2D) g;
            float[] dash = {5.0f};
            g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
            g2d.setColor(Color.BLUE);
            g2d.drawRect(x, y, width, height);

            g2d.setStroke(new BasicStroke(1.0f));
        }
    }

    @Override
    public void move(int dx, int dy) {
        // composite移動座標
        this.x += dx;
        this.y += dy;

        // 所有成員也跟著位移
        for (Shape s : members) {
            s.move(dx, dy);
        }
    }

    @Override
    public boolean contains(Point p) {
        return new Rectangle(x, y, width, height).contains(p);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        // 顯示外框，將成員的 isSelected 設為 false
        for (Shape s : members) {
            s.setSelected(false);
        }
    }
}