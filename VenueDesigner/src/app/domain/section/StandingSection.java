package app.domain.section;

import app.domain.Seat;
import app.domain.VitalSpace;
import app.domain.selection.SelectionVisitor;
import app.domain.shape.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Vector;
import java.util.function.Consumer;

public final class StandingSection extends AbstractSection {
    @JsonProperty
    private int max;

    @JsonCreator
    StandingSection(@JsonProperty("name") String name, @JsonProperty("elevation") int elevation, @JsonProperty("shape") Shape shape, @JsonProperty("max") int max) {
        super(name, elevation, shape);
        this.max = max;
    }

    public static StandingSection create(Vector<Point> points, int max) {

        Polygon polygon = new Polygon(points,new int[4]);
        StandingSection section = new StandingSection(null,0,polygon,max);
        section.max = max;
        return section;

    }

    public int getMax(){ return this.max;}


    @Override
    public void move(int x, int y) {
        getShape().move(x, y);
    }

    @Override
    public void move(int x, int y, Point offset) {
        getShape().move(x, y, offset);
    }

    @Override
    public <T> void accept(T g, Painter<T> painter) {
        getShape().accept(g, painter);
    }

    @Override
    public void accept(SelectionVisitor visitor) {
    }

    @Override
    public Seat[][] getSeats() {
        return new Seat[0][0];
    }

    @Override
    public void forEachSeats(Consumer<Seat> consumer) {
    }
}
