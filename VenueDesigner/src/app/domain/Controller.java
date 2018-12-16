package app.domain;

import app.domain.collider.Collider;
import app.domain.collider.ColliderValidator;
import app.domain.seat.Seat;
import app.domain.seat.SeatSection;
import app.domain.section.SeatedSection;
import app.domain.section.Section;
import app.domain.section.StandingSection;
import app.domain.selection.Selection;
import app.domain.selection.SelectionAdapter;
import app.domain.selection.SelectionHolder;
import app.domain.selection.SelectionVisitor;
import app.domain.shape.Point;
import app.domain.shape.Shape;
import app.domain.shape.ShapeBuilder;
import app.domain.shape.ShapeBuilderFactory;
import app.domain.section.SectionFactory;

import java.util.*;

public class Controller {
    private final Collider collider;
    private final ColliderValidator validator;
    private final SelectionHolder selectionHolder;

    private final Point cursor = new Point(-1, -1);
    private final Point offset = new Point(15, 15);

    private Room room;
    private Mode mode = Mode.None;
    private UIPanel ui;
    private ShapeBuilder current;
    private double scale = 1.0;
    private Seat hoveredSeat;
    private Section hoveredSection;
    private boolean seatHovered = false;
    private Timer timer = new Timer();
    private Observer observer;

    public Controller(Collider collider) {
        this.collider = Objects.requireNonNull(collider);
        this.validator = new ColliderValidator(collider);
        this.selectionHolder = new SelectionHolder(collider);
        this.room = new Room(900, 900, new VitalSpace(30, 30));
    }

    public Room getRoom() {
        return room;
    }

    public void setDrawingPanel(UIPanel ui) {
        this.ui = Objects.requireNonNull(ui);
    }

    public void createRoom(double roomWidth, double roomHeight, double vitalSpaceWidth, double vitalSpaceHeight) {
        room = new Room(roomWidth, roomHeight, new VitalSpace(vitalSpaceWidth, vitalSpaceHeight));
    }

    public void save(String path) {
        JSONSerialize.serializeToJson(room, path);
    }

    public void load(String path) {
        room = JSONSerialize.deserializeFromJson(path);
        ui.repaint();
    }

    public double getXCursor () {
        return  cursor.x;
    }

    public double getYCursor () {
        return  cursor.y;
    }

    public double getScale() {
        return scale;
    }

    public void mouseDragged(int x, int y) {
        observer.onLeave();
        double scaleX = x / scale;
        double scaleY = y / scale;
        double dx = scaleX - cursor.x;
        double dy = scaleY - cursor.y;
        cursor.set(scaleX, scaleY);

        if (!selectionHolder.applySelection(new SelectionVisitor() {
            @Override
            public void visit(Stage stage) {
                move(stage);
                autoSetSeat();
            }

            @Override
            public void visit(SeatedSection section) {
                move(section);
                autoSetSeat();
            }

            @Override
            public void visit(StandingSection section) {
                move(section);
            }

            @Override
            public void visit(Seat seat) {
                move(selectionHolder.getPreSelection());
            }

            @Override
            public void visit(SeatSection seatSection) {
                move(selectionHolder.getPreSelection());
            }

            private void move(Selection selection) {
                if (isMovable(selection.getShape(), scaleX, scaleY)) {
                    selection.move(scaleX, scaleY, offset);
                }
            }
        })) {
            offset.offset(dx, dy);
        }
        ui.repaint();
    }

    public void mouseClicked(int x, int y) {
        observer.onLeave();
        if (room == null) {
            return;
        }
        double scaleX = x / scale;
        double scaleY = y / scale;
        if (mode == Mode.None || mode == Mode.Selection) {
            doSelection(scaleX, scaleY);
        } else {
            doShape(scaleX, scaleY);
        }
        ui.repaint();
    }

    public void mouseMoved(int x, int y) {
        seatHovered = false;
        double scaleX = x / scale;
        double scaleY = y / scale;
        cursor.set(scaleX, scaleY);
        for (Section s: room.getSections()){
            s.forEachSeats( seat -> {
                if (selectionHolder.selectionCheck(seat.getShape(), scaleX, scaleY, offset)) {
                    Mode mode = Mode.Selection;
                    seatHovered = true;
                    if (hoveredSeat != seat) {
                        observer.onLeave();
                        hoveredSection = s;
                        hoveredSeat = seat;
                        timerReset();
                    }
                }
            });
        }
        if (!seatHovered){
            observer.onLeave();
            timer.cancel();
            timer.purge();
            timer = new Timer();
        }
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
        selectionHolder.resetSelection(room);
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
        selectionHolder.applySelection(visitor);
    }

    public void removeSelected() {
        selectionHolder.applySelection(new SelectionAdapter() {
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
        selectionHolder.applySelection(new SelectionAdapter() {
            @Override
            public void visit(Stage stage) {
                rotate(stage);
                autoSetSeat();
            }

            @Override
            public void visit(SeatedSection section) {
                rotate(section);
                autoSetSeat();
            }

            @Override
            public void visit(StandingSection section) {
                rotate(section);
            }

            private void rotate(Selection select){
                if (isRotatable(select.getShape(), direction)){
                    if (direction){
                        select.rotate(Math.PI/32);
                    } else {
                        select.rotate(-Math.PI/32);
                    }
                }
            }
        });
        ui.repaint();
    }

    public boolean isAutoSelected() {
        return selectionHolder.getSelection().map(Selection::isAuto).orElse(false);
    }

    public void autoSetSeatSelected(){
        selectionHolder.applySelection(new SelectionAdapter() {
            @Override
            public void visit(SeatedSection section) {
                section.autoSetSeat=!section.autoSetSeat;
            }
        });
    }

    public void autoSetSeat() {
        for (Section s: room.getSections()){
            if(!room.getStage().isPresent()){
                return;
            }
            s.accept(new SelectionAdapter() {
                @Override
                public void visit(SeatedSection section) {
                    if (section.autoSetSeat){
                        section.autoSetSeats(room.getStage().get(),collider);
                    }
                }
            });
        }
        ui.repaint();
    }

    public boolean createRegularSection(int x, int y, int xInt, int yInt) {
        if (!room.getStage().isPresent()) {
            return false;
        }
        Section section = SeatedSection.create(x - offset.x, y - offset.y, xInt, yInt, room.getVitalSpace(), room.getStage().get());
        if (!validator.validShape(section.getShape(), room, new Point())) {
            return false;
        }
        room.addSection(section);
        mode = Mode.None;
        return true;
    }

    private void doSelection(double x, double y) {
        mode = Mode.None;
        if (selectionHolder.checkSelection(room, x, y, offset)) {
            mode = Mode.Selection;
        }
    }

    private void doShape(double x, double y) {
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
        if (!validator.validShape(shape, room, offset)) {
            return;
        }
        for (Point p : shape.getPoints()) {
            p.x -= offset.x;
            p.y -= offset.y;
        }
        if (mode == Mode.Stage) {
            room.setStage(new Stage(shape));
        } else {
            Section s = SectionFactory.create(mode, shape, room.getVitalSpace());
            if (mode == Mode.IrregularSeatedSection){
                s.autoSetSeats(room.getStage().get(), collider);
            }
            room.addSection(s);
        }
        mode = Mode.None;
    }

    private boolean isMovable(Shape shape, double x, double y) {
        Shape predict = shape.clone();
        predict.move(x, y, offset);
        return validator.validPredictShape(shape, predict, room, new Point());
    }

    private boolean isRotatable(Shape shape, boolean direction) {
        Shape predict = shape.clone();
        if (direction){
            predict.rotate(Math.PI/32,predict.computeCentroid());
        } else {
            predict.rotate(31*Math.PI/32,predict.computeCentroid());
        }
        return validator.validPredictShape(shape, predict, room, new Point());
    }

    public void autoScaling(int panelWidth, int panelHeight) {
        double maxRoom;
        int maxPanel;
        double roomWidth = room.getWidth();
        double roomHeight = room.getHeight();
        if (roomHeight > roomWidth || roomWidth < 2 * roomHeight) {
            maxRoom = roomHeight;
            maxPanel = panelHeight;
        } else {
            maxRoom = roomWidth;
            maxPanel = panelWidth;
        }
        scale = (0.8 * maxPanel) / maxRoom;
        offset.x = (int)(((panelWidth - roomWidth * scale) / 2) / scale);
        offset.y = (int)(((panelHeight - roomHeight * scale) / 2) / scale);
        ui.repaint();
    }

    public boolean validateRoomDimensions(double roomWidth, double roomHeight, double vitalSpaceWidth, double vitalSpaceHeight) {
        VitalSpace vs = new VitalSpace(vitalSpaceWidth, vitalSpaceHeight);
        Room predict = new Room(roomWidth, roomHeight, vs);
        Optional<Stage> opt = room.getStage();
        if (opt.isPresent()) {
            if (validator.invalidShapeRoom(opt.get().getShape(), predict, new Point())) {
                return false;
            }
            for (Section section : room.getSections()) {
                if (!validator.invalidShapeRoom(section.getShape(), predict, new Point())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validateSectionDimensions(Section section, int nbColums, int nbRows, double spaceWidth, double spaceHeight) {
        VitalSpace vs = new VitalSpace(spaceWidth, spaceHeight);
        Section predict = SeatedSection.create(section.getShape().getPoints().firstElement().x, section.getShape().getPoints().firstElement().y, nbColums, nbRows, vs, room.getStage().get());
        return validator.validPredictShape(section.getShape(), predict.getShape(), room, new Point());
    }

    public boolean validateStageDimensions(Stage stage, double width, double height) {
        Shape shape = stage.getShape().clone();
        Stage predict = new Stage(shape);
        predict.setWidth(width);
        predict.setHeight(height);
        return validator.validPredictShape(stage.getShape(), predict.getShape(), room, new Point());
    }

    public void setObserver(Observer observer){
        this.observer = Objects.requireNonNull(observer);
    }
    
    private void timerReset(){
        timer.cancel();
        timer.purge();
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                observer.onHover();
            }
        };
        timer.schedule(task, 500);
    }

    public Seat getHoveredSeat() {
        return hoveredSeat;
    }

    public Section getHoveredSection() {
        return hoveredSection;
    }

    public double prepareSave(int panelWidth, int panelHeight) {
        double maxRoom;
        int maxPanel;
        double roomWidth = room.getWidth();
        double roomHeight = room.getHeight();
        if (roomHeight > roomWidth || roomWidth < 2 * roomHeight) {
            maxRoom = roomHeight;
            maxPanel = panelHeight;
        } else {
            maxRoom = roomWidth;
            maxPanel = panelWidth;
        }
        scale = (0.9 * maxPanel) / maxRoom;
        offset.x = 0;
        offset.y = 0;
        return scale;
    }

    public void offsetScale(Point offset, double scale) {
        this.offset.x = offset.x;
        this.offset.y = offset.y;
        this.scale = scale;
        ui.repaint();
    }
}
