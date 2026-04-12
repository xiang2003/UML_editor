package ui;

import controller.mode.*;
import model.CompositeObject;
import model.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CanvasArea extends JPanel {
    private List<Shape> shapes = new ArrayList<>();
    private Mode currentMode;

    public CanvasArea() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        // 預設為選取模式
        this.currentMode = new SelectMode(this);

        // 註冊滑鼠監聽，轉發給目前的 Mode
        MouseAdapter listener = new MouseAdapter() {
            @Override public void mousePressed(java.awt.event.MouseEvent e) { currentMode.mousePressed(e); }
            @Override public void mouseReleased(java.awt.event.MouseEvent e) { currentMode.mouseReleased(e); }
            @Override public void mouseDragged(java.awt.event.MouseEvent e) { currentMode.mouseDragged(e); }
            @Override public void mouseMoved(java.awt.event.MouseEvent e) { currentMode.mouseMoved(e); }
        };
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public void setMode(Mode mode) {
        // 1. 離開舊模式前，取消所有選取 (隱藏 Ports)
        deselectAll();

        // 2. 取消所有 Hover 狀態 (防止切換後 Port 殘留在畫面上)
        for (Shape s : shapes) {
            s.setHovered(false);
        }

        // 3. 切換至新模式
        this.currentMode = mode;

        // 4. 重繪畫布
        repaint();
    }
    public void addShape(Shape s) {
        shapes.add(s);
        repaint();
    }

    public List<Shape> getShapes() { return shapes; }

    public void deselectAll() {
        for (Shape s : shapes) s.setSelected(false);
        repaint();
    }

    // Use Case C: 將物件移至最上層 (Depth 最小)
    public void moveShapeToFront(Shape s) {
        if (shapes.remove(s)) {
            shapes.add(s);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape s : shapes) s.draw(g);

        // 讓模式（如 SelectMode）可以畫出框選虛線
        currentMode.paint(g);
    }
    public void groupSelected() {
        // 1. 找出所有目前被選取的物件
        List<Shape> selected = shapes.stream()
                .filter(Shape::isSelected)
                .collect(Collectors.toList());

        // Alternatives D.1: 當選取物件少於 2 個時，不執行動作
        if (selected.size() < 2) {
            System.out.println("Grouping requires at least 2 objects.");
            return;
        }

        // 2. 建立新的組合物件
        CompositeObject composite = new CompositeObject();
        for (Shape s : selected) {
            composite.addMember(s); // 將成員加入組合
            shapes.remove(s);       // 從畫布主清單移除單一物件
        }

        // 3. 將新的組合物件加入畫布，並設為選取狀態
        composite.setSelected(true);
        shapes.add(composite);

        repaint();
    }

    /**
     * Use Case D - Case 2: Ungroup objects
     */
    public void ungroupSelected() {
        // 1. 找出選取中的物件，且必須剛好是一個 CompositeObject
        List<Shape> selected = shapes.stream()
                .filter(Shape::isSelected)
                .collect(Collectors.toList());

        // Alternatives D.2: 只能對「一個」選中的群組進行解構
        if (selected.size() != 1 || !(selected.get(0) instanceof CompositeObject)) {
            System.out.println("Ungrouping requires exactly 1 composite object.");
            return;
        }

        CompositeObject target = (CompositeObject) selected.get(0);

        // 2. 取出成員，放回畫布主清單
        shapes.remove(target);
        for (Shape member : target.getMembers()) {
            member.setSelected(true); // 讓成員維持選取狀態
            shapes.add(member);
        }

        repaint();
    }
}
