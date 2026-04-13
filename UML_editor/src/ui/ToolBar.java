package ui;

import controller.EventBus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class ToolBar extends JPanel {
    private Map<String, JButton> buttons;
    private String currentPermanentMode = "select";
    public ToolBar() {
        buttons = new HashMap<>();

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
                    resetAllButtonVisuals();
                    btn.setBackground(Color.BLACK);
                    btn.setForeground(Color.WHITE);
                    btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    EventBus.getInstance().publishModeChange("prepare_" + command);
                } else {
                    currentPermanentMode = command;
                    setActiveButton(command);
                    EventBus.getInstance().publishModeChange(command);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragCreate) {
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(Color.BLACK);
                    btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                    Point screenPoint = e.getLocationOnScreen();
                    EventBus.getInstance().publishMouseReleased(command, screenPoint);
                }
            }
        });
        buttons.put(command, btn);
        add(btn);
    }
    //調整按鈕樣式
    public void resetAllButtonVisuals() {
        for (JButton btn : buttons.values()) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
    }

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