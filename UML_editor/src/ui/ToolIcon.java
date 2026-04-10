package ui;

import javax.swing.*;
import java.awt.*;

public class ToolIcon implements Icon {
    private String type;
    private int size = 30;

    public ToolIcon(String type) {
        this.type = type;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.getForeground());
        g2.setStroke(new BasicStroke(1.5f));

        switch (type.toLowerCase()) {
            case "select" -> {
                // 畫箭頭
                int[] px = {x+5, x+25, x+15, x+15};
                int[] py = {y+5, y+15, y+15, y+25};
                g2.fillPolygon(px, py, 4);
            }
            case "association" -> {
                g2.drawLine(x+5, y+15, x+25, y+15);
                g2.drawLine(x+5, y+15, x+12, y+10);
                g2.drawLine(x+5, y+15, x+12, y+20);
            }
            case "generalization" -> {
                g2.drawLine(x+15, y+15, x+25, y+15);
                g2.drawPolygon(new int[]{x+5, x+15, x+15}, new int[]{y+15, y+10, y+20}, 3);
            }
            case "composition" -> {
                g2.drawLine(x+18, y+15, x+25, y+15);
                g2.drawPolygon(new int[]{x+5, x+12, x+19, x+12}, new int[]{y+15, y+10, y+15, y+20}, 4);
            }
            case "rect" -> {
                g2.setColor(Color.GRAY);
                g2.fillRect(x+7, y+5, 16, 20);
                g2.setColor(Color.DARK_GRAY);
                g2.drawRect(x+7, y+5, 16, 20);
            }
            case "oval" -> {
                g2.setColor(Color.GRAY);
                g2.fillOval(x+5, y+7, 20, 16);
                g2.setColor(Color.DARK_GRAY);
                g2.drawOval(x+5, y+7, 20, 16);
            }
        }
        g2.dispose();
    }

    @Override public int getIconWidth() { return size; }
    @Override public int getIconHeight() { return size; }
}