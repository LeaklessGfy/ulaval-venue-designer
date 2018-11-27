package app.domain.selection;

import app.domain.shape.Point;
import app.domain.shape.Shape;

public interface Selection {
    boolean isSelected();
    void setSelected(boolean selected);
    void move(int x, int y);
    void move(int x, int y, Point offset);
    Shape getShape();
    void accept(SelectionVisitor visitor);
}
