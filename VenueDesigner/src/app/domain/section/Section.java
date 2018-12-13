package app.domain.section;

import app.domain.Drawable;
import app.domain.Seat;
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
    void setPrice(int price);
    void setName(String name);
    String getName();
    int getElevation();
    void setElevation(int elevation);
    Seat[][] getSeats();
    Shape getShape();
    void setShape(Shape shape);
    void move(int x, int y, Point offset);
    void forEachSeats(Consumer<Seat> consumer);
}
