package ui.dialogs;

import model.Shape;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class LabelDialog extends JDialog {
    private JTextField nameField;
    private JSlider rSlider, gSlider, bSlider;
    private JLabel rLabel, gLabel, bLabel; // 顯示數字用
    private JPanel colorPreview;
    private Shape target;

    public LabelDialog(Frame parent, Shape target) {
        super(parent, "Customize Color (RGB)", true);
        this.target = target;
        setLayout(new BorderLayout(15, 15));

        // --- 上方：文字標籤輸入 ---
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 15));
        topPanel.add(new JLabel("Label Name:"), BorderLayout.WEST);
        nameField = new JTextField(target.getLabel());
        topPanel.add(nameField, BorderLayout.CENTER);

        // --- 中間：滑桿區 (左) 與 預覽區 (右) ---
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // 左側滑桿區
        JPanel slidersPanel = new JPanel();
        slidersPanel.setLayout(new BoxLayout(slidersPanel, BoxLayout.Y_AXIS));

        Color current = target.getBgColor();
        rSlider = createRGBSelection("Red", current.getRed());
        gSlider = createRGBSelection("Green", current.getGreen());
        bSlider = createRGBSelection("Blue", current.getBlue());

        slidersPanel.add(createSliderComponent("R: ", rSlider, rLabel = new JLabel(String.valueOf(current.getRed()))));
        slidersPanel.add(Box.createVerticalStrut(10)); // 間距
        slidersPanel.add(createSliderComponent("G: ", gSlider, gLabel = new JLabel(String.valueOf(current.getGreen()))));
        slidersPanel.add(Box.createVerticalStrut(10));
        slidersPanel.add(createSliderComponent("B: ", bSlider, bLabel = new JLabel(String.valueOf(current.getBlue()))));

        // 右側預覽區
        JPanel previewBox = new JPanel(new BorderLayout(5, 5));
        colorPreview = new JPanel();
        colorPreview.setPreferredSize(new Dimension(120, 120));
        colorPreview.setBorder(BorderFactory.createTitledBorder("Color Preview"));
        updatePreview(); // 初始化預覽
        previewBox.add(colorPreview, BorderLayout.CENTER);

        centerPanel.add(slidersPanel);
        centerPanel.add(previewBox);

        // --- 下方：按鈕區 ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(e -> {
            target.setLabel(nameField.getText());
            target.setBgColor(colorPreview.getBackground());
            dispose();
        });
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());
        bottomPanel.add(okBtn);
        bottomPanel.add(cancelBtn);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    // 輔助方法：建立滑桿並綁定監聽器
    private JSlider createRGBSelection(String title, int initialValue) {
        JSlider slider = new JSlider(0, 255, initialValue);
        slider.addChangeListener(e -> {
            updatePreview();
            // 同時更新數字標籤
            if (rLabel != null) rLabel.setText(String.valueOf(rSlider.getValue()));
            if (gLabel != null) gLabel.setText(String.valueOf(gSlider.getValue()));
            if (bLabel != null) bLabel.setText(String.valueOf(bSlider.getValue()));
        });
        return slider;
    }

    // 輔助方法：把標籤、滑桿、數字包在一起
    private JPanel createSliderComponent(String prefix, JSlider slider, JLabel valLabel) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.add(new JLabel(prefix), BorderLayout.WEST);
        p.add(slider, BorderLayout.CENTER);
        valLabel.setPreferredSize(new Dimension(30, 20));
        p.add(valLabel, BorderLayout.EAST);
        return p;
    }

    private void updatePreview() {
        int r = rSlider.getValue();
        int g = gSlider.getValue();
        int b = bSlider.getValue();
        colorPreview.setBackground(new Color(r, g, b));
    }
}