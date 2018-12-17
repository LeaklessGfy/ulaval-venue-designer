package app.domain;

import app.domain.seat.Seat;

import java.util.ArrayList;
import java.util.Objects;

public final class Offer {
    public enum DiscountMode {
        Dollar,
        Percent
    }

    private final ArrayList<Seat> seats = new ArrayList<>();
    private String name;
    private DiscountMode mode;
    private int discount;

    public Offer(String name, DiscountMode mode, int discount) {
        this.name = Objects.requireNonNull(name);
        this.mode = Objects.requireNonNull(mode);
        this.discount = discount;
    }

    public double Discount(String discountMode, int originPrice,int valueDiscount ){
        double p;
        if(discountMode.equals("%")){
            double price = (double) Math.round(((double)originPrice - ((valueDiscount/100f)*originPrice)) * 100) / 100; // 9.456 --> 9.46;
            if(price < 0){
                p = 0;
            }else{
                p = price;
            }
        }else{
            double price = (double) Math.round(((double)originPrice - valueDiscount) * 100) / 100;
            if (price < 0) {
                p = 0;
            }else{
                p = price;
            }
        }
        return p;
    }


    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public DiscountMode getMode() {
        return mode;
    }

    public void setMode(DiscountMode mode) {
        this.mode = Objects.requireNonNull(mode);
    }

    public int getDiscount(){
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return name;
    }
}
