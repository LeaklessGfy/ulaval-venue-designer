package app.domain.section;

import app.domain.Seat;
import app.domain.shape.Painter;
import app.domain.shape.Point;
import app.domain.shape.Shape;

import java.util.Vector;

public final class StandingSection extends AbstractSection {
    private int max;

    StandingSection(String name, int elevation, Shape shape, int max) {
        super(name, elevation, shape);
        this.max = max;
    }

    public static StandingSection create(int max, Shape shape) {
        return new StandingSection(null, 0, shape, max);
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
    public Vector<Seat> getSeats(){
        return new Vector<>();
    }
}


