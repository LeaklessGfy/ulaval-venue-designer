package app.domain;

public final class Offer {

    private double price;
    private String name;
    private String discountMode;
    private int valueDiscount;

    public Offer(String name, String discountMode, int valueDiscount){
        this.name = name;
        this.discountMode = discountMode;
        this.valueDiscount = valueDiscount;
    }

    public void Discount(String discountMode, int originPrice,int valueDiscount ){
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
        }
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

    public String getDiscountMode(){
        return discountMode;
    }

    @Override
    public String toString() {
        return name;
    }
}
