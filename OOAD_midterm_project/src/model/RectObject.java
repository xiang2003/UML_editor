package model;


import java.awt.*;

public class RectObject extends BasicObject {

    public RectObject(int x, int y) {
        super(x, y, 100, 100);
        initializePorts();
    }

    private void initializePorts() {
        // 8 個點：四個角 + 四條邊的中點
        double[] locs = {0.0, 0.5, 1.0};
        for (double rx : locs) {
            for (double ry : locs) {
                if (rx == 0.5 && ry == 0.5) continue; // 跳過正中心
                ports.add(new Port(rx, ry));
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        // --- 1. 背景顏色切換 (Use Case G) ---
        // 根據 labelColor 屬性決定顏色
        g.setColor(bgColor);
        g.fillRect(x, y, width, height);

        // --- 2. 畫外框 ---
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        // --- 3. 標籤文字置中 (Use Case G) ---
        if (label != null && !label.isEmpty()) {
            g.setColor(Color.BLACK); // 文字固定黑色
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(label);
            int textHeight = fm.getAscent();

            // 計算置中座標
            int tx = x + (width - textWidth) / 2;
            int ty = y + (height + textHeight) / 2 - 2; // 稍微微調
            g.drawString(label, tx, ty);
        }

        drawPorts(g);
    }
    /**
     * 輔助方法：將標籤顏色字串轉為 AWT Color 物件
     */
    private Color getAwtColor(String colorName) {
        if (colorName == null) return Color.LIGHT_GRAY;
        return switch (colorName.toLowerCase()) {
            case "yellow" -> Color.YELLOW;
            case "red" -> Color.RED;
            case "white" -> Color.WHITE;
            default -> Color.LIGHT_GRAY; // 預設淺灰色
        };
    }


    @Override
    public boolean contains(Point p) {
        return new Rectangle(x, y, width, height).contains(p);
    }
}