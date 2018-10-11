package app.domain;

public final class Seat {
    private final int column;
    private final int row;

    private int price;
    private int color;

    public Seat(int column, int row) {
        this.column = column;
        this.row = row;
    }
}
