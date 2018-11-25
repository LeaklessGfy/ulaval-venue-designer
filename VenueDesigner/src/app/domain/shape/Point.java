package app.domain.shape;

public final class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
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
