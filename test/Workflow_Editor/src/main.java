import javax.swing.*;

class CanvasArea extends JPanel {
    private List<Shape> shapes = new ArrayList<>();
    private List<Link> links = new ArrayList<>();
    private Mode currentMode;

    public CanvasArea() {
        currentMode = new SelectMode(); // 預設 Select
        // 註冊滑鼠監聽器，將事件導向 currentMode
    }

    public void setMode(Mode mode) { this.currentMode = mode; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 根據 Depth 排序繪製 (Use Case 2 Definitions)
        shapes.sort(Comparator.comparingInt(s -> -s.depth));
        for (Shape s : shapes) s.draw(g);
        for (Link l : links) l.draw(g);
    }

    // Group 功能
    public void groupSelected() {
        List<Shape> selected = new ArrayList<>();
        // 找尋 isSelected == true 的物件
        if (selected.size() >= 2) {
            CompositeObject group = new CompositeObject();
            // add to group and remove from shapes list
            shapes.add(group);
        }
    }
}