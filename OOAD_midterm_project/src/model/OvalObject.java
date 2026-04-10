package model;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class OvalObject extends BasicObject {

    public OvalObject(int x, int y) {
        super(x, y, 100, 80);
        initializePorts();
    }

    private void initializePorts() {
        // 這裡的座標是相對比例（0.0 ~ 1.0），由 Port 類別計算實際位置
        // 或是直接定義位置邏輯
        ports.add(new Port(0.5, 0.0)); // Top
        ports.add(new Port(0.5, 1.0)); // Bottom
        ports.add(new Port(0.0, 0.5)); // Left
        ports.add(new Port(1.0, 0.5)); // Right
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 背景變色
        g2.setColor(bgColor);
        g2.fillOval(x, y, width, height);

        g2.setColor(Color.BLACK);
        g2.drawOval(x, y, width, height);

        // 文字置中
        if (label != null && !label.isEmpty()) {
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            int tx = x + (width - fm.stringWidth(label)) / 2;
            int ty = y + (height + fm.getAscent()) / 2 - 2;
            g2.drawString(label, tx, ty);
        }

        drawPorts(g);
    }

    private Color getAwtColor(String colorName) {
        if (colorName == null) return Color.LIGHT_GRAY;
        return switch (colorName.toLowerCase()) {
            case "yellow" -> Color.YELLOW;
            case "red" -> Color.RED;
            case "white" -> Color.WHITE;
            default -> Color.LIGHT_GRAY;
        };
    }

    @Override
    public boolean contains(Point p) {
        Ellipse2D ellipse = new Ellipse2D.Double(x, y, width, height);
        return ellipse.contains(p);
    }
}