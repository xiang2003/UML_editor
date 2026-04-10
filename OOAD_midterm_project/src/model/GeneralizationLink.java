package model;

import java.awt.*;

public class GeneralizationLink extends Link {

    public GeneralizationLink(BasicObject source, Port sourcePort, BasicObject target, Port targetPort) {
        super(source, sourcePort, target, targetPort);
    }

    @Override
    protected void drawArrow(Graphics g, Point start, Point end) {
        double angle = Math.atan2(end.y - start.y, end.x - start.x);
        int size = 12;

        // 計算三角形的三個頂點
        int[] xPoints = {
                end.x,
                (int) (end.x - size * Math.cos(angle - Math.PI / 6)),
                (int) (end.x - size * Math.cos(angle + Math.PI / 6))
        };
        int[] yPoints = {
                end.y,
                (int) (end.y - size * Math.sin(angle - Math.PI / 6)),
                (int) (end.y - size * Math.sin(angle + Math.PI / 6))
        };

        // 畫空心三角形
        g.setColor(Color.WHITE);
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 3);
    }
}