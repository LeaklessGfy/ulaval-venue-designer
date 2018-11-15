package app.domain.section;

import app.domain.shape.Shape;

public final class StandingSection extends AbstractSection {
    private int max;

    StandingSection(String name, int elevation, Shape shape, int max) {
        super(name, elevation, shape);
        this.max = max;
    }

    public static StandingSection create(int max, Shape shape) {
        return new StandingSection(null, 0, shape, max);
    }
}
