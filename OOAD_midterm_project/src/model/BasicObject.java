package model;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BasicObject extends Shape {
    protected List<Port> ports = new ArrayList<>();

    public BasicObject(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    protected void drawPorts(Graphics g) {
        if (isSelected||isHovered) {
            for (Port p : ports) {
                p.draw(g, this.x, this.y, this.width, this.height);
            }
        }
    }

    public Port findPortAt(Point p) {
        for (Port port : ports) {
            if (port.contains(p, x, y, width, height)) {
                return port;
            }
        }
        return null;
    }
}