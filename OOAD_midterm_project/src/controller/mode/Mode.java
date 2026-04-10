package controller.mode;

import java.awt.*;
import java.awt.event.MouseEvent;

public interface Mode {
    void mousePressed(MouseEvent e);
    void mouseReleased(MouseEvent e);
    void mouseDragged(MouseEvent e);
    void mouseMoved(MouseEvent e);
    void mouseClicked(MouseEvent e);
    void paint(Graphics g);

}