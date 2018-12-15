package app.domain.section;

import app.domain.Collider;
import app.domain.Seat;
import app.domain.Stage;
import app.domain.selection.SelectionVisitor;
import app.domain.shape.Painter;
import app.domain.shape.Point;
import app.domain.shape.Shape;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.Consumer;

public final class StandingSection extends AbstractSection {
    @JsonProperty
    private int max;

    @JsonProperty
    private double price;


    @JsonCreator
    StandingSection(@JsonProperty("name") String name, @JsonProperty("elevation") int elevation,
                    @JsonProperty("shape") Shape shape, @JsonProperty("max") int max, @JsonProperty("price") double price) {
        super(name, elevation, shape);
        this.max = max;
        this.price =price;
    }

    public static StandingSection create(int max, Shape shape) {
        return new StandingSection(null, 0, shape, max,0);
    }

    @Override
    public void move(double x, double y, Point offset) {
        getShape().move(x, y, offset);
    }

    @Override
    public <T> void accept(T g, Painter<T> painter) {
        getShape().accept(g, painter);
    }

    @Override
    public void accept(SelectionVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Seat[][] getSeats() {
        return new Seat[0][0];
    }

    @Override
    public void forEachSeats(Consumer<Seat> consumer) {
    }

    @Override
    public void rotate(double thetaRadian){
        super.getShape().rotate(thetaRadian, getShape().computeCentroid());
    }

    @Override
    public void autoSetSeats(Stage stage, Collider collider){
    }

    public void setPrice(double price){
        this.price=price;
    }

    public void setMax(int max){
        this.max=max;
    }
    public int getMax(){
        return max;
    }

    public double getPrice(){
        return price;
    }

    @Override
    public boolean isAuto(){
        return false;
    }
}
