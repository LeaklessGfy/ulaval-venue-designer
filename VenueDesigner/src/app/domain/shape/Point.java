package app.domain.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Point {
    public int x;
    public int y;

    @JsonCreator
    public Point(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
        this(0, 0);
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void offset(int x, int y) {
        this.x += x;
        this.y += y;
    }
}
