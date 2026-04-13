package controller.mode;

import model.*;
import ui.CanvasArea;
import java.awt.event.MouseEvent;

public class CreateLinkMode extends DefaultMode {
    private CanvasArea canvas;
    private String linkType; // "association", "generalization", "composition"

    private BasicObject sourceObj = null;
    private Port sourcePort = null;

    public CreateLinkMode(CanvasArea canvas, String linkType) {
        this.canvas = canvas;
        this.linkType = linkType;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (int i = canvas.getShapes().size() - 1; i >= 0; i--) {
            Shape s = canvas.getShapes().get(i);

            if (s instanceof BasicObject basic) {
                Port p = basic.findPortAt(e.getPoint());
                if (p != null) {
                    sourceObj = basic;
                    sourcePort = p;
                    System.out.println("Source port identified.");
                    return;
                }
            }
        }
        // 如果沒點在 Port 上
        sourceObj = null;
        sourcePort = null;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // 如果沒點到起點 Port
        if (sourceObj == null) return;

        for (int i = canvas.getShapes().size() - 1; i >= 0; i--) {
            Shape s = canvas.getShapes().get(i);

            // 不是起點物件
            if (s instanceof BasicObject targetObj && targetObj != sourceObj) {
                Port targetPort = targetObj.findPortAt(e.getPoint());

                if (targetPort != null) {
                    Link link = createLinkInstance(sourceObj, sourcePort, targetObj, targetPort);
                    canvas.addShape(link);
                    System.out.println("Link created: " + linkType);
                    break;
                }
            }
        }

        sourceObj = null;
        sourcePort = null;
        canvas.repaint();
    }


    private Link createLinkInstance(BasicObject src, Port srcP, BasicObject dest, Port destP) {
        return switch (linkType.toLowerCase()) {
            case "association" -> new AssociationLink(src, srcP, dest, destP);
            case "generalization" -> new GeneralizationLink(src, srcP, dest, destP);
            case "composition" -> new CompositionLink(src, srcP, dest, destP);
            default -> throw new IllegalArgumentException("Unknown link type: " + linkType);
        };
    }
}