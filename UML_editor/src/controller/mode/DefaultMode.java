package controller.mode;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class DefaultMode implements Mode {
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void paint(Graphics g) {}
}