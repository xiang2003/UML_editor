package model;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class OvalObject extends BasicObject {

    public OvalObject(int x, int y) {
        super(x, y, 100, 80);
        initializePorts();
    }

    private void initializePorts() {
        ports.add(new Port(0.5, 0.0)); // Top
        ports.add(new Port(0.5, 1.0)); // Bottom
        ports.add(new Port(0.0, 0.5)); // Left
        ports.add(new Port(1.0, 0.5)); // Right
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(bgColor);
        g2.fillOval(x, y, width, height);

        g2.setColor(Color.BLACK);
        g2.drawOval(x, y, width, height);

        drawLabelAndPorts(g);
    }

    @Override
    public boolean contains(Point p) {
        Ellipse2D ellipse = new Ellipse2D.Double(x, y, width, height);
        return ellipse.contains(p);
    }
}