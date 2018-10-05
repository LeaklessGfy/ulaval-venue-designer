package app.domain;

import app.domain.section.Section;

import java.util.ArrayList;
import java.util.Objects;

public final class Room {
    private final ArrayList<Section> sections = new ArrayList<>();

    private int width;
    private int height;
    private double scale = 1.0;
    private VitalSpace vitalSpace;
    private boolean grid;
    private Stage stage;

    public Room(int width, int height, VitalSpace vitalSpace) {
        this.width = width;
        this.height = height;
        this.vitalSpace = Objects.requireNonNull(vitalSpace);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
