package app.domain;

import app.domain.section.SeatedSection;
import app.domain.section.Section;
import app.domain.section.StandingSection;
import app.domain.selection.Selection;
import app.domain.selection.SelectionAdapter;
import app.domain.selection.SelectionVisitor;
import app.domain.shape.Point;
import app.domain.shape.Shape;
import app.domain.shape.ShapeBuilder;
import app.domain.shape.ShapeBuilderFactory;
import app.domain.section.SectionFactory;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

public class Controller {
    private final Collider collider;
    private final Point cursor = new Point(-1, -1);
    private final Point offset = new Point(0, 0);
    private final HashMap<Mode, BiConsumer<Integer, Integer>> clickActions = new HashMap<>();

    private Room room;
    private Mode mode = Mode.None;
    private UIPanel ui;
    private ShapeBuilder current;
    private double scale = 1.0;
    private Selection selection;
    private Section preSelection;

    public Controller(Collider collider) {
        this.collider = Objects.requireNonNull(collider);
        this.room = new Room(900, 900, new VitalSpace(30, 30));
    }

    public Room getRoom() {
        return room;
    }

    public void setDrawingPanel(UIPanel ui) {
        this.ui = Objects.requireNonNull(ui);
    }

    public void createRoom(int roomWidth, int roomHeight, int vitalSpaceWidth, int vitalSpaceHeight) {
        room = new Room(roomWidth, roomHeight, new VitalSpace(vitalSpaceWidth, vitalSpaceHeight));
    }

    public void save(String path) {
        JSONSerialize.serializeToJson(room, path);
    }

    public void load(String path) {
        room = JSONSerialize.deserializeFromJson(path);
        ui.repaint();
    }

    public int getXCursor () {
        return  cursor.x;
    }

    public int getYCursor () {
        return  cursor.y;
    }

    public double getScale() {
        return scale;
    }

    public void mouseDragged(int x, int y) {
        int scaleX = (int)(x / scale);
        int scaleY = (int)(y / scale);
        int dx = (scaleX - cursor.x);
        int dy = (scaleY - cursor.y);
        cursor.set(scaleX, scaleY);

        if (selection != null) {
            selection.accept(new SelectionVisitor() {
                @Override
                public void visit(Stage stage) {
                    move(stage);
                }

                @Override
                public void visit(SeatedSection section) {
                    move(section);
                }

                @Override
                public void visit(StandingSection section) {
                    move(section);
                }

                @Override
                public void visit(Seat seat) {
                    move(preSelection);
                }

                @Override
                public void visit(SeatSection seatSection) {
                    move(preSelection);
                }

                private void move(Selection selection) {
                    if (isMovable(selection.getShape(), scaleX, scaleY)) {
                        selection.move(scaleX, scaleY, offset);
                    }
                }
            });
        } else {
            offset.offset(dx, dy);
        }
        ui.repaint();
    }
    public void mouseClicked(int x, int y) {
        if (room == null) {
            return;
        }
        int scaleX = (int)(x / scale);
        int scaleY = (int)(y / scale);
        if (mode == Mode.None || mode == Mode.Selection) {
            doSelection(scaleX, scaleY);
        } else {
            doShape(scaleX, scaleY);
        }
        ui.repaint();
    }

    public void mouseMoved(int x, int y) {
        int scaleX = (int)(x / scale);
        int scaleY = (int)(y / scale);
        cursor.set(scaleX, scaleY);
        ui.repaint();
    }

    public void mouseWheelMoved(double rotation) {
        this.scale += (0.1 *-(rotation));
        ui.repaint();
    }

    public void zoom(double scale) {
        this.scale += scale;
        ui.repaint();
    }

    public boolean toggleMode(Mode mode) {
        if (room == null) {
            return false;
        }
        current = null;
        if (this.mode == mode) {
            this.mode = Mode.None;
            return false;
        }
        this.mode = Objects.requireNonNull(mode);
        resetSelection();
        ui.repaint();
        return true;
    }

    public Optional<ShapeBuilder> getCurrent() {
        return Optional.ofNullable(current);
    }

    public Mode getMode() {
        return mode;
    }

    public Point getOffset() {
        return offset;
    }

    public void editSelected(SelectionVisitor visitor) {
        if (selection == null) {
            return;
        }
        selection.accept(visitor);
    }

    public void removeSelected() {
        if (selection == null) {
            return;
        }
        selection.accept(new SelectionAdapter() {
            @Override
            public void visit(Stage stage) {
                room.setStage(null);
                mode = Mode.None;
            }

            @Override
            public void visit(SeatedSection section) {
                room.getSections().remove(section);
            }

            @Override
            public void visit(StandingSection section) {
                room.getSections().remove(section);
            }
        });
        ui.repaint();
    }

    public void rotateSelected(boolean direction) {
        if (selection == null) {
            return;
        }
        selection.accept(new SelectionAdapter() {
            @Override
            public void visit(Stage stage) {
                if (direction){
                    selection.rotate(Math.PI/32);
                } else {
                   selection.rotate(31*Math.PI/32);
                }
            }

            @Override
            public void visit(SeatedSection section) {
                if (direction){
                    selection.rotate(Math.PI/16);
                } else {
                    selection.rotate(31*Math.PI/16);
                }
            }

            @Override
            public void visit(StandingSection section) {
                if (direction){
                    selection.rotate(Math.PI/16);
                } else {
                    selection.rotate(31*Math.PI/16);
                }
            }
        });
        ui.repaint();
    }

    public void autoSetSeatSelected() {
        if (selection == null) {
            return;
        }
        selection.accept(new SelectionAdapter() {
            @Override
            public void visit(Stage stage) {}

            @Override
            public void visit(SeatedSection section) {
                if(!room.getStage().isPresent()){
                    //TODO:ajouter messsage indiquant le besoin d'une scène pour ce feature
                    return;
                }
                section.autoSetSeats(room.getStage().get(),collider);
            }

            @Override
            public void visit(StandingSection section) { }
        });
        ui.repaint();
    }

    public void createRegularSection(int x, int y, int xInt, int yInt) {
        if (room != null && room.getStage().isPresent()) {
            Section section = SeatedSection.create(x - offset.x, y - offset.y, xInt, yInt, room.getVitalSpace(), room.getStage().get());
            if (!room.validShape(section.getShape(), new Point())) {
                return;
            }
            if (room.getStage().isPresent()) {
                if (collider.hasCollide(room.getStage().get().getShape(), section.getShape())) {
                    return;
                }
            }
            for (Section s : room.getSections()) {
                if (collider.hasCollide(s.getShape(), section.getShape())) {
                    return;
                }
            }
            room.addSection(section);
            mode = Mode.None;
        }
    }

    private void doSelection(int x, int y) {
        mode = Mode.None;
        Selection s = selection;
        resetSelection();
        if (s != null) {
            s.accept(new SelectionAdapter() {
                @Override
                public void visit(SeatedSection section) {
                    section.forEachSeats(seat -> {
                        if (selectionCheck(x, y, seat.getShape())) {
                            selection = seat;
                            selection.setSelected(true);
                            preSelection.setSelected(true);
                        }
                    });
                }

                @Override
                public void visit(Seat seat) {
                    Seat[] seats = preSelection.getSeats()[seat.getRow()];
                    for (Seat s : seats) {
                        if (selectionCheck(x, y, s.getShape())) {
                            selection = new SeatSection(seats);
                            selection.setSelected(true);
                            preSelection.setSelected(true);
                        }
                    }
                }
            });
            return;
        }
        Optional<Stage> opt = room.getStage();
        if (opt.isPresent()) {
            Stage stage = opt.get();
            if (selectionCheck(x, y, stage.getShape())) {
                selection = stage;
                selection.setSelected(true);
                return;
            }
        }
        for (Section section : room.getSections()) {
            if (selectionCheck(x, y, section.getShape())) {
                selection = section;
                selection.setSelected(true);
                preSelection = section;
                return;
            }
        }
    }

    private void doShape(int x, int y) {
        if (current == null) {
            current = ShapeBuilderFactory.create(mode);
        }
        current.addPoint(new Point(x, y));
        if (current.isComplete()) {
            createShape();
        }
    }

    private void createShape() {
        current.correctLastPoint();
        Shape shape = current.build();
        current = null;
        if (!room.validShape(shape, offset)) {
            return;
        }
        if (room.getStage().isPresent()) {
            if (collider.hasCollide(room.getStage().get().getShape(), shape)) {
                return;
            }
        }
        for (Section section : room.getSections()) {
            if (collider.hasCollide(shape, section.getShape())) {
                return;
            }
        }
        for (Point p : shape.getPoints()) {
            p.x -= offset.x;
            p.y -= offset.y;
        }
        if (mode == Mode.Stage) {
            room.setStage(new Stage(shape));
        } else {
            room.addSection(SectionFactory.create(mode, shape));
        }
        mode = Mode.None;
    }

    private boolean isMovable(Shape shape, int x, int y) {
        Shape predict = shape.clone();
        predict.move(x, y, offset);
        if (!room.validShape(predict, new Point())) {
            return false;
        }
        if (room.getStage().isPresent() && shape != room.getStage().get().getShape()) {
            if (collider.hasCollide(room.getStage().get().getShape(), predict)) {
                return false;
            }
        }
        for (Section section : room.getSections()) {
            if (section.getShape() != shape && collider.hasCollide(section.getShape(), predict)) {
                return false;
            }
        }
        return true;
    }

    private boolean selectionCheck(int x, int y, Shape shape){
        if (collider.hasCollide(x - offset.x, y - offset.y, shape)){
            mode = Mode.Selection;
            return true;
        }
        return false;
    }

    private void resetSelection() {
        if (selection != null) {
            selection.setSelected(false);
            selection = null;
        }
        room.getStage().ifPresent(stage -> stage.setSelected(false));
        room.getSections().forEach(section -> {
            section.setSelected(false);
            section.forEachSeats(seat -> seat.setSelected(false));
        });
    }

    public boolean validateSectionDimensions(Section section, int nbColums, int nbRows, int spaceWidth, int spaceHeight) {
        VitalSpace vs = new VitalSpace(spaceWidth, spaceHeight);
        Section predict = SeatedSection.create(section.getShape().getPoints().firstElement().x, section.getShape().getPoints().firstElement().y, nbColums, nbRows, vs, room.getStage().get());
        if (!room.validShape(predict.getShape(), new Point())) {
            return false;
        }
        if (room.getStage().isPresent()) {
            if (collider.hasCollide(room.getStage().get().getShape(), predict.getShape())) {
                return false;
            }
        }
        for (Section s : room.getSections()) {
            if (!s.equals(section)) {
                if (collider.hasCollide(s.getShape(), predict.getShape())) {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean validateStageDimensions(Stage stage, int width, int height) {
        Shape shape = stage.getShape().clone();
        Stage predict = new Stage(shape);
        predict.setWidth(width);
        predict.setHeight(height);
        if (!room.validShape(predict.getShape(), new Point())) {
            return false;
        }
        for (Section s : room.getSections()) {
            if (collider.hasCollide(s.getShape(), predict.getShape())) {
                return false;
            }
        }
        return true;
    }
}
