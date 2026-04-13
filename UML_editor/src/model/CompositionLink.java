package model;

import java.awt.*;

//菱形型連線
public class CompositionLink extends Link {

    public CompositionLink(BasicObject source, Port sourcePort, BasicObject target, Port targetPort) {
        super(source, sourcePort, target, targetPort);
    }

    @Override
    protected void drawArrow(Graphics g, Point start, Point end) {
        double angle = Math.atan2(end.y - start.y, end.x - start.x);
        int size = 10;

        // 菱形頂點座標
        int[] xPoints = {
                start.x,
                (int) (start.x + size * Math.cos(angle - Math.PI / 4)),
                (int) (start.x + size * 2 * Math.cos(angle)),
                (int) (start.x + size * Math.cos(angle + Math.PI / 4))
        };
        int[] yPoints = {
                start.y,
                (int) (start.y + size * Math.sin(angle - Math.PI / 4)),
                (int) (start.y + size * 2 * Math.sin(angle)),
                (int) (start.y + size * Math.sin(angle + Math.PI / 4))
        };

        //畫空心菱形
        g.setColor(Color.WHITE);
        g.fillPolygon(xPoints, yPoints, 4);
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 4);
    }
}