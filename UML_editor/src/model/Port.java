package model;

import java.awt.*;

public class Port {
    private double relativeX, relativeY; // 相對位置 (0.0 ~ 1.0)
    public static final int SIZE = 8;    // 黑色小方塊的大小

    public Port(double rx, double ry) {
        this.relativeX = rx;
        this.relativeY = ry;
    }
    // 在 Port.java 中加上
    public double getRelativeX() { return relativeX; }
    public double getRelativeY() { return relativeY; }

    public void draw(Graphics g, int parentX, int parentY, int parentW, int parentH) {
        g.setColor(Color.BLACK);
        int px = (int) (parentX + relativeX * parentW) - SIZE / 2;
        int py = (int) (parentY + relativeY * parentH) - SIZE / 2;
        g.fillRect(px, py, SIZE, SIZE);
    }

    public boolean contains(Point p, int parentX, int parentY, int parentW, int parentH) {
        int px = (int) (parentX + relativeX * parentW);
        int py = (int) (parentY + relativeY * parentH);

        // 增加感應邊距，例如感應範圍設為 12 像素
        int hitArea = 12;
        Rectangle area = new Rectangle(px - hitArea / 2, py - hitArea / 2, hitArea, hitArea);
        return area.contains(p);
    }
}