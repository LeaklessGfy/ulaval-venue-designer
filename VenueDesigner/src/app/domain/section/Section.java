package app.domain.section;

import app.domain.Collider;
import app.domain.Drawable;
import app.domain.Seat;
import app.domain.Stage;
import app.domain.selection.Selection;
import app.domain.shape.Point;
import app.domain.shape.Shape;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.function.Consumer;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AbstractSection.class, name = "AbstractSection")
})
public interface Section extends Drawable, Selection {
    String getName();
    int getElevation();
    void setElevation(int elevation);
    Seat[][] getSeats();
    Shape getShape();
    void setShape(Shape shape);
    void move(int x, int y, Point offset);
    void forEachSeats(Consumer<Seat> consumer);
    void autoSetSeats(Stage stage, Collider collider);
}
