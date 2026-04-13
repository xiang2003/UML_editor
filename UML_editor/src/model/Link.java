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
    //繪製線段及箭頭
    @Override
    public void draw(Graphics g) {
        Point start = getPortLocation(source, sourcePort);
        Point end = getPortLocation(target, targetPort);

        g.setColor(Color.BLACK);
        g.drawLine(start.x, start.y, end.x, end.y);

        drawArrow(g, start, end);
    }

    protected abstract void drawArrow(Graphics g, Point start, Point end);


    protected Point getPortLocation(BasicObject obj, Port port) {
        int px = (int) (obj.x + port.getRelativeX() * obj.width);
        int py = (int) (obj.y + port.getRelativeY() * obj.height);
        return new Point(px, py);
    }

    @Override
    public boolean contains(Point p) {
        return false;
    }
}