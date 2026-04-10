package model;

import java.awt.*;

public class AssociationLink extends Link {

    public AssociationLink(BasicObject source, Port sourcePort, BasicObject target, Port targetPort) {
        super(source, sourcePort, target, targetPort);
    }

    @Override
    protected void drawArrow(Graphics g, Point start, Point end) {
        // 畫實心或空心箭頭 (這裡示範畫一個簡單的單向箭頭)
        double angle = Math.atan2(end.y - start.y, end.x - start.x);
        int arrowSize = 10;

        int x1 = (int) (end.x - arrowSize * Math.cos(angle - Math.PI / 6));
        int y1 = (int) (end.y - arrowSize * Math.sin(angle - Math.PI / 6));
        int x2 = (int) (end.x - arrowSize * Math.cos(angle + Math.PI / 6));
        int y2 = (int) (end.y - arrowSize * Math.sin(angle + Math.PI / 6));

        g.drawLine(end.x, end.y, x1, y1);
        g.drawLine(end.x, end.y, x2, y2);
    }
}