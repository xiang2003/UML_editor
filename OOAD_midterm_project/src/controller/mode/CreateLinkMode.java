package controller.mode;

import model.*;
import ui.CanvasArea;
import java.awt.event.MouseEvent;

public class CreateLinkMode extends DefaultMode {
    private CanvasArea canvas;
    private String linkType; // "association", "generalization", "composition"

    // 暫存起點資訊
    private BasicObject sourceObj = null;
    private Port sourcePort = null;

    public CreateLinkMode(CanvasArea canvas, String linkType) {
        this.canvas = canvas;
        this.linkType = linkType;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Use Case B - Step 1: 在 Port 的範圍內按下 mouse
        // 逆序遍歷，確保點擊到的是最上層的物件
        for (int i = canvas.getShapes().size() - 1; i >= 0; i--) {
            Shape s = canvas.getShapes().get(i);

            // 只有 BasicObject (Rect, Oval) 才有 Ports，Composite 沒有
            if (s instanceof BasicObject basic) {
                Port p = basic.findPortAt(e.getPoint());
                if (p != null) {
                    sourceObj = basic;
                    sourcePort = p;
                    System.out.println("Source port identified.");
                    return; // 找到起點就停止搜尋
                }
            }
        }
        // Alternatives B.1: 如果沒點在 Port 上，sourceObj 會維持 null
        sourceObj = null;
        sourcePort = null;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // 如果沒點到起點 Port，直接結束
        if (sourceObj == null) return;

        // Use Case B - Step 3: 在另一個 Port 的範圍內放開 mouse
        for (int i = canvas.getShapes().size() - 1; i >= 0; i--) {
            Shape s = canvas.getShapes().get(i);

            // 條件：必須是 BasicObject、不是起點物件本人 (Alternatives B.2)
            if (s instanceof BasicObject targetObj && targetObj != sourceObj) {
                Port targetPort = targetObj.findPortAt(e.getPoint());

                if (targetPort != null) {
                    // 符合所有條件，建立 Link 物件
                    Link link = createLinkInstance(sourceObj, sourcePort, targetObj, targetPort);
                    canvas.addShape(link);
                    System.out.println("Link created: " + linkType);
                    break;
                }
            }
        }

        // 重置狀態，準備下一次連線
        sourceObj = null;
        sourcePort = null;
        canvas.repaint();
    }

    /**
     * 利用 Java 17 的 switch expression 進行簡單工廠判斷
     */
    private Link createLinkInstance(BasicObject src, Port srcP, BasicObject dest, Port destP) {
        return switch (linkType.toLowerCase()) {
            case "association" -> new AssociationLink(src, srcP, dest, destP);
            case "generalization" -> new GeneralizationLink(src, srcP, dest, destP);
            case "composition" -> new CompositionLink(src, srcP, dest, destP);
            default -> throw new IllegalArgumentException("Unknown link type: " + linkType);
        };
    }
}