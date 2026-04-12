package model;

import java.awt.*;

public abstract class Shape {
    protected int x, y;
    protected int width, height;
    protected int depth; //物件深度 0-99
    protected String label = "";
    protected Color bgColor = Color.LIGHT_GRAY;
    protected boolean isSelected = false;
    protected boolean isHovered = false;
    public Shape(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void draw(Graphics g);

    // 碰撞偵測：判斷 (x,y) 是否落在物件範圍內
    public abstract boolean contains(Point p);

    // 位移邏輯
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { this.isSelected = selected; }
    public boolean isHovered() { return isHovered; }
    public void setHovered(boolean hovered) { this.isHovered = hovered; }

    public int getDepth() { return depth; }
    public void setDepth(int depth) { this.depth = depth; }

    public void setWidth(int width) {
        this.width = width;
    }
    public int getWidth() { return this.width; }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getHeight() { return this.height; }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public Color getBgColor() { return bgColor; }
    public void setBgColor(Color color) { this.bgColor = color; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}