package model;

import java.awt.*;

public class RectObject extends BasicObject {

    public RectObject(int x, int y) {
        super(x, y, 100, 100);
        initializePorts();
    }

    private void initializePorts() {
        // 8 個點
        double[] locs = {0.0, 0.5, 1.0};
        for (double rx : locs) {
            for (double ry : locs) {
                if (rx == 0.5 && ry == 0.5) continue;
                ports.add(new Port(rx, ry));
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(bgColor);
        g.fillRect(x, y, width, height);

        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        drawLabelAndPorts(g);
    }

    @Override
    public boolean contains(Point p) {
        return new Rectangle(x, y, width, height).contains(p);
    }
}