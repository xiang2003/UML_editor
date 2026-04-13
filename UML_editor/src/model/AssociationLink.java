package model;

import java.awt.*;

//箭頭型連線
public class AssociationLink extends Link {

    public AssociationLink(BasicObject source, Port sourcePort, BasicObject target, Port targetPort) {
        super(source, sourcePort, target, targetPort);
    }

    @Override
    protected void drawArrow(Graphics g, Point start, Point end) {
        double angle = Math.atan2(end.y - start.y, end.x - start.x);
        int size = 10;

        int x1 = (int) (end.x - size * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (end.y - size * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (end.x - size * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (end.y - size * Math.sin(angle + Math.PI / 6));

        g.drawLine(end.x, end.y, x1, y1);
        g.drawLine(end.x, end.y, x2, y2);
    }
}