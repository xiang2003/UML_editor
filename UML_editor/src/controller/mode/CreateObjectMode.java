package controller.mode;

import model.RectObject;
import model.OvalObject;
import model.Shape;
import ui.CanvasArea;

import java.awt.Point;

public class CreateObjectMode extends DefaultMode {
    private CanvasArea canvas;
    private String shapeType;

    public CreateObjectMode(CanvasArea canvas, String shapeType) {
        this.canvas = canvas;
        this.shapeType = shapeType;
    }

    public void executeCreate(Point p) {
        Shape newShape = null;
        if (shapeType.equalsIgnoreCase("rect")) {
            newShape = new RectObject(p.x, p.y);
        } 
        else if(shapeType.equalsIgnoreCase("oval")){
            newShape = new OvalObject(p.x, p.y);
        }

        canvas.addShape(newShape);
        System.out.println("Object created: " + shapeType);
    }

}