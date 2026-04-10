package controller.mode;


import model.RectObject;
import model.OvalObject;
import ui.CanvasArea; // 假設畫布提供 addShape 方法
import java.awt.event.MouseEvent;

public class CreateObjectMode extends DefaultMode {
    private String shapeType;
    private CanvasArea canvas;

    public CreateObjectMode(CanvasArea canvas, String shapeType) {
        this.canvas = canvas;
        this.shapeType = shapeType;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Use Case A: 根據類型在點擊處建立物件
        if (shapeType.equalsIgnoreCase("rect")) {
            canvas.addShape(new RectObject(e.getX(), e.getY()));
        } else if (shapeType.equalsIgnoreCase("oval")) {
            canvas.addShape(new OvalObject(e.getX(), e.getY()));
        }
        canvas.repaint();
    }
}