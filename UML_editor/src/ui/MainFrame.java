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
    private String currentActiveMode = "select"; // 預設為select mode

    public MainFrame() {
        setTitle("UML editor");
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

        EventBus.getInstance().subscribeModeChange(command -> {
            // 如果是 Prepare 指令（按下rect/oval），不改變 currentActiveMode
            if (command.startsWith("prepare_")) {
                return;
            }
            // 紀錄目前的模式
            this.currentActiveMode = command;

            switchMode(command);
        });

        EventBus.getInstance().subscribeMouseReleased((type, screenPoint) -> {
            Point canvasPoint = new Point(screenPoint);
            SwingUtilities.convertPointFromScreen(canvasPoint, canvas);

            if (canvas.getBounds().contains(canvasPoint)) {
                CreateObjectMode creator = new CreateObjectMode(canvas, type);
                creator.executeCreate(canvasPoint);
            }

            // 還原先前模式
            toolbar.setActiveButton(currentActiveMode);
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

        JMenu fileMenu = new JMenu("File");
        mb.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");

        JMenuItem groupItem = new JMenuItem("Group");
        groupItem.addActionListener(e -> canvas.groupSelected());
        editMenu.add(groupItem);

        JMenuItem ungroupItem = new JMenuItem("Ungroup");
        ungroupItem.addActionListener(e -> canvas.ungroupSelected());
        editMenu.add(ungroupItem);

        editMenu.addSeparator();

        JMenuItem labelItem = new JMenuItem("Change Label");
        labelItem.addActionListener(e -> {
            // 取得目前畫布上所有被選取的物件
            List<Shape> selectedShapes = canvas.getShapes().stream()
                    .filter(Shape::isSelected)
                    .toList();

            // 判斷選取數量是否剛好為 1
            if (selectedShapes.size() == 1) {
                Shape target = selectedShapes.get(0);

                new LabelDialog(this, target).setVisible(true);

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