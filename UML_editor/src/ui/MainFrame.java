package ui;

import controller.EventBus;
import controller.mode.*;
import ui.dialogs.LabelDialog;
import model.*;
import model.Shape;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private CanvasArea canvas;
    private ToolBar toolbar;
    // 在 MainFrame 類別內部定義
    private String currentActiveMode = "select"; // 預設紀錄為 select
    public MainFrame() {
        setTitle("Workflow Design Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout(10, 10));
        javax.swing.ToolTipManager.sharedInstance().setInitialDelay(50);

        canvas = new CanvasArea();
        toolbar = new ToolBar();

        // 設置選單
        setJMenuBar(createMenuBar());

        add(toolbar, BorderLayout.WEST);
        add(canvas, BorderLayout.CENTER);

        // 訂閱 EventBus，當按鈕點下時，切換 Canvas 的模式
// 在 MainFrame.java 的建構子中

// 1. 訂閱準備建立物件的通知
        EventBus.getInstance().subscribeModeChange(command -> {
            // 如果是 Prepare 指令（按下方塊/橢圓），我們不需要改變 currentActiveMode
            if (command.startsWith("prepare_")) {
                return;
            }

            // 紀錄目前的真實模式 (為了 Use Case A 結束後還原)
            this.currentActiveMode = command;

            // 根據指令切換 Canvas 模式
            switchMode(command);
        });

// 2. 訂閱滑鼠放開的通知 (處理跨組件拖曳)
        EventBus.getInstance().subscribeMouseReleased((type, screenPoint) -> {
            Point canvasPoint = new Point(screenPoint);
            SwingUtilities.convertPointFromScreen(canvasPoint, canvas);

            if (canvas.getBounds().contains(canvasPoint)) {
                if (type.equals("rect")) canvas.addShape(new RectObject(canvasPoint.x, canvasPoint.y));
                else if (type.equals("oval")) canvas.addShape(new OvalObject(canvasPoint.x, canvasPoint.y));
            }

            // --- Use Case A - Step 6: 關鍵還原邏輯 ---
            // 還原 UI 按鈕顏色到「前一個模式」
            toolbar.setActiveButton(currentActiveMode);

            // 還原 Canvas 行為到「前一個模式」
            switchMode(currentActiveMode);

            canvas.repaint();
        });
    }
    private void switchMode(String command) {
        Mode nextMode = switch (command) {
            case "select" -> new SelectMode(canvas);
            case "association", "generalization", "composition" -> new CreateLinkMode(canvas, command);
            default -> new SelectMode(canvas);
        };
        canvas.setMode(nextMode);
        System.out.println("Current Mode is now: " + command);
    }
    private JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar();

        // 1. File 選單 (目前 PDF 沒要求功能，保留結構)
        JMenu fileMenu = new JMenu("File");
        mb.add(fileMenu);

        // 2. Edit 選單
        JMenu editMenu = new JMenu("Edit");

        // --- Group 選項 (Use Case D) ---
        JMenuItem groupItem = new JMenuItem("Group");
        groupItem.addActionListener(e -> canvas.groupSelected());
        editMenu.add(groupItem);

        // --- Ungroup 選項 (Use Case D) ---
        JMenuItem ungroupItem = new JMenuItem("Ungroup");
        ungroupItem.addActionListener(e -> canvas.ungroupSelected());
        editMenu.add(ungroupItem);

        // 分隔線
        editMenu.addSeparator();

        // --- Change Label 選項 (Use Case G) ---
        JMenuItem labelItem = new JMenuItem("Change Label");
        labelItem.addActionListener(e -> {
            // 邏輯：從畫布中找出目前「唯一被選取」的物件
            // 因為 Label Dialog 通常針對單一基本物件修改
            // 1. 取得目前畫布上所有被選取的物件
            List<Shape> selectedShapes = canvas.getShapes().stream()
                    .filter(Shape::isSelected)
                    .toList();

            // 2. 判斷選取數量是否剛好為 1
            if (selectedShapes.size() == 1) {
                // 取得那唯一的一個物件
                Shape target = selectedShapes.get(0);

                // 開啟 Dialog (傳入 this 作為 parent，以及選中的物件)
                new LabelDialog(this, target).setVisible(true);

                // 視窗關閉後（OK 被按下後），重繪畫布
                canvas.repaint();
            }
            else if (selectedShapes.isEmpty()) {
                // 提示使用者未選取
                JOptionPane.showMessageDialog(this,
                        "Please select an object first.",
                        "No Object Selected",
                        JOptionPane.WARNING_MESSAGE);
            }
            else {
                // 提示使用者選取太多
                JOptionPane.showMessageDialog(this,
                        "You can only change color for ONE object at a time.\n(Currently selected: " + selectedShapes.size() + ")",
                        "Multiple Selection Conflict",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        editMenu.add(labelItem);

        mb.add(editMenu);
        return mb;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}