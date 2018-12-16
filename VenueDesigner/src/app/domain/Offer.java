package app.domain;

import java.util.ArrayList;

public final class Offer {

    private  ArrayList<Seat> Lseat = new ArrayList();
    public static ArrayList<Offer> Loffer = new ArrayList();
    private double price;
    private String name;
    private String discountMode;
    private int valueDiscount;

    public Offer(String name, String discountMode, int valueDiscount){
        this.name = name;
        this.discountMode = discountMode;
        this.valueDiscount = valueDiscount;
    }

    public double Discount(String discountMode, int originPrice,int valueDiscount ){
        if(discountMode.equals("%")){
            double price = (double) Math.round(((double)originPrice - ((valueDiscount/100f)*originPrice)) * 100) / 100; // 9.456 --> 9.46;
            if(price < 0){
                this.price = 0;
            }else{this.price = price;}
        }else{
            double price = (double) Math.round(((double)originPrice - valueDiscount) * 100) / 100;
            if(price < 0){
                this.price = 0;
            }else{this.price = price;}
        }return price;
    }

    public int getDiscountPrice(){
        return this.valueDiscount;
    }

   /* public void setDiscountPrice(double Discountprice){
        this.price = Discountprice;
    }*/

    public String getName(){
        return name;
    }


    public void setLoffer(ArrayList<Offer> offer){this.Loffer = offer;}

    public ArrayList<Offer> getLoffer(){ return Loffer;}

    public String getDiscountMode(){
        return discountMode;
    }

    @Override
    public String toString() {
        return name;
    }
}
