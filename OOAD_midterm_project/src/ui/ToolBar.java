package ui;

import controller.EventBus;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class ToolBar extends JPanel {
    private Map<String, JButton> buttons = new HashMap<>();
    private String currentPermanentMode = "select";
    public ToolBar() {
        setLayout(new GridLayout(6, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        setBackground(Color.WHITE);

        createIconButton("Select", "select", false);
        createIconButton("Association", "association", false);
        createIconButton("Generalization", "generalization", false);
        createIconButton("Composition", "composition", false);
        createIconButton("Rect", "rect", true);
        createIconButton("Oval", "oval", true);

        setActiveButton("select");
    }

    private void createIconButton(String tooltip, String command, boolean isDragCreate) {
        JButton btn = new JButton();
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setIcon(new ToolIcon(command));
        btn.setToolTipText(tooltip);

        // 初始樣式
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (isDragCreate) {
                    // --- Use Case A - 步驟 2 ---
                    // 1. 先把「原本變黑的那個按鈕」變回白色
                    resetAllButtonVisuals();
                    // 2. 讓現在這個按鈕變黑
                    btn.setBackground(Color.BLACK);
                    btn.setForeground(Color.WHITE);
                    btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    EventBus.getInstance().publishModeChange("prepare_" + command);
                } else {
                    // 一般模式切換
                    currentPermanentMode = command; // 紀錄為永久模式
                    setActiveButton(command);
                    EventBus.getInstance().publishModeChange(command);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragCreate) {
                    // --- Use Case A - 步驟 6 (前半部) ---
                    // 點擊結束，先把自己的顏色洗掉
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(Color.BLACK);
                    btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                    // 發送訊息讓 MainFrame 去處理「還原原模式」
                    Point screenPoint = e.getLocationOnScreen();
                    EventBus.getInstance().publishMouseReleased(command, screenPoint);
                }
            }
        });
        buttons.put(command, btn);
        add(btn);
    }
    public void resetAllButtonVisuals() {
        for (JButton btn : buttons.values()) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
    }
    /**
     * 核心邏輯：控制背景變黑
     */
    public void setActiveButton(String command) {
        resetAllButtonVisuals();
        JButton target = buttons.get(command);
        if (target != null) {
            target.setBackground(Color.BLACK);
            target.setForeground(Color.WHITE);
            target.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    }
}