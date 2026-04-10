package model;

import java.awt.*;

public abstract class Link extends Shape {
    protected BasicObject source; // 起點物件
    protected BasicObject target; // 終點物件
    protected Port sourcePort;    // 起點的連接點
    protected Port targetPort;    // 終點的連接點

    public Link(BasicObject source, Port sourcePort, BasicObject target, Port targetPort) {
        // 連線的位置與大小由連接的物件動態決定，因此父類別傳入 0,0,0,0
        super(0, 0, 0, 0);
        this.source = source;
        this.sourcePort = sourcePort;
        this.target = target;
        this.targetPort = targetPort;
    }

    @Override
    public void draw(Graphics g) {
        // 物件會移動，所以每次繪製時都要重新計算絕對座標
        Point start = getPortLocation(source, sourcePort);
        Point end = getPortLocation(target, targetPort);

        g.setColor(Color.BLACK);
        // 1. 畫主線段
        g.drawLine(start.x, start.y, end.x, end.y);

        // 2. 多型展現：由子類別去畫各自的箭頭
        drawArrow(g, start, end);
    }

    // 留給子類別實作專屬的箭頭
    protected abstract void drawArrow(Graphics g, Point start, Point end);

    /**
     * 輔助方法：根據 BasicObject 的位置與 Port 的相對位置，計算出畫布上的絕對座標
     */
    protected Point getPortLocation(BasicObject obj, Port port) {
        int px = (int) (obj.x + port.getRelativeX() * obj.width);
        int py = (int) (obj.y + port.getRelativeY() * obj.height);
        return new Point(px, py);
    }

    @Override
    public boolean contains(Point p) {
        // 連線的碰撞偵測較複雜（判斷點到線段的距離），在簡易編輯器中通常回傳 false，
        // 或點擊物件來選取，PDF 中未特別要求選取連線，暫不實作。
        return false;
    }
}